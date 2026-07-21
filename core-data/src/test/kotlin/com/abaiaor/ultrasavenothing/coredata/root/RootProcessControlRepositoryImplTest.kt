package com.abaiaor.ultrasavenothing.coredata.root

import android.content.Context
import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledApp
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RootProcessControlRepositoryImplTest {

    private fun buildRepository(
        installedApps: List<InstalledApp> = emptyList(),
        allowedPackageNames: Set<String> = emptySet(),
        dialerPackageName: String? = null,
        messagingPackageName: String? = null,
        appPackageName: String = "com.abaiaor.ultrasavenothing",
    ): Pair<RootProcessControlRepositoryImpl, FakeRootShellExecutor> {
        val rootShellExecutor = FakeRootShellExecutor()
        val context = mockk<Context> {
            every { packageName } returns appPackageName
        }
        val repository = RootProcessControlRepositoryImpl(
            rootShellExecutor = rootShellExecutor,
            installedAppsRepository = FakeInstalledAppsRepository(installedApps),
            allowlistRepository = FakeAllowlistRepository(allowedPackageNames),
            appLaunchRepository = FakeAppLaunchRepository(dialerPackageName, messagingPackageName),
            context = context,
        )
        return repository to rootShellExecutor
    }

    @Test
    fun `WHEN sweeping THEN non-allowed non-protected apps are force-stopped`() = runTest {
        val (repository, rootShellExecutor) = buildRepository(
            installedApps = listOf(
                InstalledApp("com.allowed.app", "Allowed"),
                InstalledApp("com.random.app", "Random"),
                InstalledApp("com.other.app", "Other"),
            ),
            allowedPackageNames = setOf("com.allowed.app"),
        )

        repository.sweepOnce()

        assertEquals(
            setOf("am force-stop com.random.app", "am force-stop com.other.app"),
            rootShellExecutor.executedCommands.toSet(),
        )
    }

    @Test
    fun `WHEN sweeping THEN this app itself is never force-stopped`() = runTest {
        val (repository, rootShellExecutor) = buildRepository(
            installedApps = listOf(InstalledApp("com.abaiaor.ultrasavenothing", "This app")),
            appPackageName = "com.abaiaor.ultrasavenothing",
        )

        repository.sweepOnce()

        assertTrue(rootShellExecutor.executedCommands.isEmpty())
    }

    @Test
    fun `WHEN sweeping THEN default dialer and messaging apps are never force-stopped`() = runTest {
        val (repository, rootShellExecutor) = buildRepository(
            installedApps = listOf(
                InstalledApp("com.dialer.app", "Dialer"),
                InstalledApp("com.sms.app", "SMS"),
            ),
            dialerPackageName = "com.dialer.app",
            messagingPackageName = "com.sms.app",
        )

        repository.sweepOnce()

        assertTrue(rootShellExecutor.executedCommands.isEmpty())
    }

    @Test
    fun `WHEN all installed apps are allowed THEN nothing is force-stopped`() = runTest {
        val (repository, rootShellExecutor) = buildRepository(
            installedApps = listOf(InstalledApp("com.allowed.app", "Allowed")),
            allowedPackageNames = setOf("com.allowed.app"),
        )

        repository.sweepOnce()

        assertTrue(rootShellExecutor.executedCommands.isEmpty())
    }

    @Test
    fun `WHEN stop is called without start THEN nothing throws`() {
        val (repository, _) = buildRepository()

        repository.stop()
    }
}
