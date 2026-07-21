package com.abaiaor.ultrasavenothing.coresystem.applaunch

/**
 * Launches apps from the minimal Ultra Mode shell: a specific installed app by package name,
 * or the device's current default Phone/Messaging app for the always-pinned entries.
 */
interface AppLaunchRepository {

    /** Launches the app identified by [packageName], if it exposes a launcher entry point. */
    fun launchApp(packageName: String)

    /** Launches the device's current default Phone (dialer) app. */
    fun launchPhoneApp()

    /** Launches the device's current default Messaging (SMS) app. */
    fun launchMessagingApp()

    /** The package name of the device's current default Phone (dialer) app, if resolvable. */
    fun defaultDialerPackageName(): String?

    /** The package name of the device's current default Messaging (SMS) app, if resolvable. */
    fun defaultMessagingPackageName(): String?
}
