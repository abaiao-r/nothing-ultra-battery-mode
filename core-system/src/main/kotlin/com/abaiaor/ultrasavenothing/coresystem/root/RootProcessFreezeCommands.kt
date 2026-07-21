package com.abaiaor.ultrasavenothing.coresystem.root

/** Pure construction of the raw root shell command used to force-stop a single app. */
object RootProcessFreezeCommands {

    fun forceStopCommand(packageName: String): String = "am force-stop $packageName"
}
