package com.abaiaor.ultrasavenothing.coresystem.root

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RootSystemProfileRepositoryImplTest {

    private val coreDirs = listOf(
        "/sys/devices/system/cpu/cpu0",
        "/sys/devices/system/cpu/cpu1",
    )

    private lateinit var executor: FakeRootShellExecutor
    private lateinit var repository: RootSystemProfileRepositoryImpl

    @Before
    fun setUp() {
        executor = FakeRootShellExecutor(
            outputsByCommand = mapOf(
                RootProfileCommands.readNightModeCommand() to listOf("1"),
                RootProfileCommands.listCpuCoreDirsCommand() to coreDirs,
                RootProfileCommands.readCpuMaxFrequencyCommand(coreDirs[0]) to listOf("2400000"),
                RootProfileCommands.readCpuMaxFrequencyCommand(coreDirs[1]) to listOf("2200000"),
            ),
        )
        repository = RootSystemProfileRepositoryImpl(executor)
    }

    @Test
    fun `WHEN applying the profile THEN battery saver wifi and mobile data are all disabled`() {
        repository.applyUltraProfile()

        assertTrue(RootProfileCommands.applyBatterySaverCommand() in executor.executedCommands)
        assertTrue(RootProfileCommands.disableWifiCommand() in executor.executedCommands)
        assertTrue(RootProfileCommands.disableMobileDataCommand() in executor.executedCommands)
    }

    @Test
    fun `WHEN applying the profile THEN dark theme is forced after reading the previous value`() {
        repository.applyUltraProfile()

        val nightModeCommands = executor.executedCommands.filter {
            it == RootProfileCommands.readNightModeCommand() || it == RootProfileCommands.forceDarkThemeCommand()
        }
        assertEquals(
            listOf(RootProfileCommands.readNightModeCommand(), RootProfileCommands.forceDarkThemeCommand()),
            nightModeCommands,
        )
    }

    @Test
    fun `WHEN applying the profile THEN every discovered cpu core is capped`() {
        repository.applyUltraProfile()

        coreDirs.forEach { coreDir ->
            assertTrue(RootProfileCommands.capCpuFrequencyCommand(coreDir) in executor.executedCommands)
        }
    }

    @Test
    fun `WHEN reverting after applying THEN battery saver wifi and mobile data are all restored`() {
        repository.applyUltraProfile()
        executor.executedCommands.clear()

        repository.revertUltraProfile()

        assertTrue(RootProfileCommands.revertBatterySaverCommand() in executor.executedCommands)
        assertTrue(RootProfileCommands.enableWifiCommand() in executor.executedCommands)
        assertTrue(RootProfileCommands.enableMobileDataCommand() in executor.executedCommands)
    }

    @Test
    fun `WHEN reverting after applying THEN the previously read night mode and cpu frequencies are restored`() {
        repository.applyUltraProfile()
        executor.executedCommands.clear()

        repository.revertUltraProfile()

        assertTrue(RootProfileCommands.restoreNightModeCommand("1") in executor.executedCommands)
        assertTrue(
            RootProfileCommands.restoreCpuFrequencyCommand(coreDirs[0], "2400000") in executor.executedCommands,
        )
        assertTrue(
            RootProfileCommands.restoreCpuFrequencyCommand(coreDirs[1], "2200000") in executor.executedCommands,
        )
    }

    @Test
    fun `WHEN reverting without a prior apply THEN no crash and no restore commands are run`() {
        repository.revertUltraProfile()

        assertTrue(
            executor.executedCommands.none {
                it.startsWith("echo") || it == RootProfileCommands.restoreNightModeCommand("1")
            },
        )
    }
}
