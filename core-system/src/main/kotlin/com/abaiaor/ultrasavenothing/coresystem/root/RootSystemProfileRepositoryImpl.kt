package com.abaiaor.ultrasavenothing.coresystem.root

import com.abaiaor.ultrasavenothing.coresystem.profile.SystemProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Root-powered implementation of [SystemProfileRepository]: unlike
 * `SystemProfileRepositoryImpl` (which can only nudge the user towards Battery Saver settings),
 * this actually flips Android's real Battery Saver, disables Wi-Fi/mobile data, forces dark
 * theme, and caps CPU max frequency on every core — all via root shell commands built by
 * [RootProfileCommands] and executed by [RootShellExecutor].
 *
 * Only bound via Hilt when [com.abaiaor.ultrasavenothing.coresystem.root.RootCapabilityRepository]
 * reports root is available; the non-root [SystemProfileRepository] implementation is used
 * otherwise, so `:feature-ultra-mode` never needs to know which one is active.
 */
@Singleton
class RootSystemProfileRepositoryImpl @Inject constructor(
    private val rootShellExecutor: RootShellExecutor,
) : SystemProfileRepository {

    private var previousNightMode: String? = null
    private var previousMaxFrequencyByCoreDir: Map<String, String> = emptyMap()

    override fun applyUltraProfile() {
        rootShellExecutor.run(RootProfileCommands.applyBatterySaverCommand())
        rootShellExecutor.run(RootProfileCommands.disableWifiCommand())
        rootShellExecutor.run(RootProfileCommands.disableMobileDataCommand())

        previousNightMode = rootShellExecutor.run(RootProfileCommands.readNightModeCommand())
            .firstOrNull()
        rootShellExecutor.run(RootProfileCommands.forceDarkThemeCommand())

        val coreDirs = rootShellExecutor.run(RootProfileCommands.listCpuCoreDirsCommand())
        previousMaxFrequencyByCoreDir = coreDirs.associateWith { coreDir ->
            rootShellExecutor.run(RootProfileCommands.readCpuMaxFrequencyCommand(coreDir))
                .firstOrNull()
                .orEmpty()
        }
        coreDirs.forEach { coreDir ->
            rootShellExecutor.run(RootProfileCommands.capCpuFrequencyCommand(coreDir))
        }
    }

    override fun revertUltraProfile() {
        rootShellExecutor.run(RootProfileCommands.revertBatterySaverCommand())
        rootShellExecutor.run(RootProfileCommands.enableWifiCommand())
        rootShellExecutor.run(RootProfileCommands.enableMobileDataCommand())

        previousNightMode?.let { nightMode ->
            rootShellExecutor.run(RootProfileCommands.restoreNightModeCommand(nightMode))
        }
        previousMaxFrequencyByCoreDir.forEach { (coreDir, originalFrequencyKhz) ->
            if (originalFrequencyKhz.isNotBlank()) {
                rootShellExecutor.run(
                    RootProfileCommands.restoreCpuFrequencyCommand(coreDir, originalFrequencyKhz),
                )
            }
        }

        previousNightMode = null
        previousMaxFrequencyByCoreDir = emptyMap()
    }
}
