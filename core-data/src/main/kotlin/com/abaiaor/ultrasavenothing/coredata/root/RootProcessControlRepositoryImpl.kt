package com.abaiaor.ultrasavenothing.coredata.root

import android.content.Context
import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository
import com.abaiaor.ultrasavenothing.coresystem.applaunch.AppLaunchRepository
import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledAppsRepository
import com.abaiaor.ultrasavenothing.coresystem.root.NonAllowedPackagesCalculator
import com.abaiaor.ultrasavenothing.coresystem.root.RootProcessFreezeCommands
import com.abaiaor.ultrasavenothing.coresystem.root.RootShellExecutor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Real implementation of [RootProcessControlRepository]: every [SWEEP_INTERVAL_MILLIS], computes
 * every installed app not on the live allowlist (excluding this app itself and the device's
 * default dialer/messaging apps) via [NonAllowedPackagesCalculator], and force-stops each one
 * via [RootShellExecutor]. On a non-rooted device the force-stop commands simply fail silently
 * (see [RootShellExecutor]), so this is always safe to start.
 */
@Singleton
class RootProcessControlRepositoryImpl @Inject constructor(
    private val rootShellExecutor: RootShellExecutor,
    private val installedAppsRepository: InstalledAppsRepository,
    private val allowlistRepository: AllowlistRepository,
    private val appLaunchRepository: AppLaunchRepository,
    @ApplicationContext private val context: Context,
) : RootProcessControlRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var sweepJob: Job? = null

    override fun start() {
        if (sweepJob?.isActive == true) return
        sweepJob = scope.launch {
            while (isActive) {
                sweepOnce()
                delay(SWEEP_INTERVAL_MILLIS)
            }
        }
    }

    override fun stop() {
        sweepJob?.cancel()
        sweepJob = null
    }

    /** Visible internally so tests can verify a single sweep's behavior without real delays. */
    internal suspend fun sweepOnce() {
        val installedPackageNames = installedAppsRepository.getInstalledApps()
            .map { it.packageName }
            .toSet()
        val allowedPackageNames = allowlistRepository.allowedPackageNames.first()
        val packagesToForceStop = NonAllowedPackagesCalculator.packagesToForceStop(
            installedPackageNames = installedPackageNames,
            allowedPackageNames = allowedPackageNames,
            protectedPackageNames = protectedPackageNames(),
        )
        packagesToForceStop.forEach { packageName ->
            rootShellExecutor.run(RootProcessFreezeCommands.forceStopCommand(packageName))
        }
    }

    private fun protectedPackageNames(): Set<String> = setOfNotNull(
        context.packageName,
        appLaunchRepository.defaultDialerPackageName(),
        appLaunchRepository.defaultMessagingPackageName(),
    )

    private companion object {
        const val SWEEP_INTERVAL_MILLIS = 5 * 60 * 1000L
    }
}
