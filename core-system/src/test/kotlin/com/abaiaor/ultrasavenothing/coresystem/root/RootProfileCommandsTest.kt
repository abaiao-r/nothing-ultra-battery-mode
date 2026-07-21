package com.abaiaor.ultrasavenothing.coresystem.root

import org.junit.Assert.assertEquals
import org.junit.Test

class RootProfileCommandsTest {

    @Test
    fun `WHEN applying battery saver THEN command sets low_power to 1`() {
        assertEquals("settings put global low_power 1", RootProfileCommands.applyBatterySaverCommand())
    }

    @Test
    fun `WHEN reverting battery saver THEN command sets low_power to 0`() {
        assertEquals("settings put global low_power 0", RootProfileCommands.revertBatterySaverCommand())
    }

    @Test
    fun `WHEN toggling wifi THEN commands use svc wifi`() {
        assertEquals("svc wifi disable", RootProfileCommands.disableWifiCommand())
        assertEquals("svc wifi enable", RootProfileCommands.enableWifiCommand())
    }

    @Test
    fun `WHEN toggling mobile data THEN commands use svc data`() {
        assertEquals("svc data disable", RootProfileCommands.disableMobileDataCommand())
        assertEquals("svc data enable", RootProfileCommands.enableMobileDataCommand())
    }

    @Test
    fun `WHEN forcing dark theme THEN command sets ui_night_mode to 2`() {
        assertEquals("settings put secure ui_night_mode 2", RootProfileCommands.forceDarkThemeCommand())
    }

    @Test
    fun `WHEN restoring night mode THEN command uses the given previous value`() {
        assertEquals(
            "settings put secure ui_night_mode 1",
            RootProfileCommands.restoreNightModeCommand("1"),
        )
    }

    @Test
    fun `WHEN capping cpu frequency THEN command writes the capped value to the given core dir`() {
        assertEquals(
            "echo 1000000 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq",
            RootProfileCommands.capCpuFrequencyCommand("/sys/devices/system/cpu/cpu0"),
        )
    }

    @Test
    fun `WHEN restoring cpu frequency THEN command writes the original value to the given core dir`() {
        assertEquals(
            "echo 2400000 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq",
            RootProfileCommands.restoreCpuFrequencyCommand("/sys/devices/system/cpu/cpu0", "2400000"),
        )
    }

    @Test
    fun `WHEN reading cpu max frequency THEN command cats the core's scaling_max_freq file`() {
        assertEquals(
            "cat /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq",
            RootProfileCommands.readCpuMaxFrequencyCommand("/sys/devices/system/cpu/cpu1"),
        )
    }
}
