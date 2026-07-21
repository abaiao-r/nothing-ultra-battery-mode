package com.abaiaor.ultrasavenothing.coresystem.root

/**
 * Runs a raw root shell command and returns its stdout, one line per element. Kept as a thin,
 * fakeable seam between [RootProfileCommands] (pure command-building/parsing logic, unit
 * testable) and the real `su` process (libsu), which can only be exercised on a rooted device.
 */
interface RootShellExecutor {

    /** Runs [command] as root and returns its stdout lines, or an empty list on any failure. */
    fun run(command: String): List<String>
}
