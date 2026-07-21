package com.abaiaor.ultrasavenothing.coresystem.root

/**
 * Pure, unit-testable construction of the raw root shell commands used to apply/revert Ultra
 * Mode's real system-level profile: Android's own Battery Saver, Wi-Fi/mobile data radios,
 * dark theme, and a CPU max-frequency cap. Deliberately separated from [RootShellExecutor] so
 * this logic can be verified without a rooted device — only the actual execution needs one.
 */
object RootProfileCommands {

    /** A conservative CPU cap applied to every core while Ultra Mode is on, in kHz. */
    const val CAPPED_CPU_FREQUENCY_KHZ = "1000000"

    fun applyBatterySaverCommand(): String = "settings put global low_power 1"

    fun revertBatterySaverCommand(): String = "settings put global low_power 0"

    fun disableWifiCommand(): String = "svc wifi disable"

    fun enableWifiCommand(): String = "svc wifi enable"

    fun disableMobileDataCommand(): String = "svc data disable"

    fun enableMobileDataCommand(): String = "svc data enable"

    fun readNightModeCommand(): String = "settings get secure ui_night_mode"

    fun forceDarkThemeCommand(): String = "settings put secure ui_night_mode 2"

    fun restoreNightModeCommand(previousValue: String): String =
        "settings put secure ui_night_mode $previousValue"

    fun listCpuCoreDirsCommand(): String = "ls -d /sys/devices/system/cpu/cpu[0-9]*"

    fun readCpuMaxFrequencyCommand(coreDir: String): String =
        "cat $coreDir/cpufreq/scaling_max_freq"

    fun capCpuFrequencyCommand(coreDir: String): String =
        "echo $CAPPED_CPU_FREQUENCY_KHZ > $coreDir/cpufreq/scaling_max_freq"

    fun restoreCpuFrequencyCommand(coreDir: String, originalFrequencyKhz: String): String =
        "echo $originalFrequencyKhz > $coreDir/cpufreq/scaling_max_freq"
}
