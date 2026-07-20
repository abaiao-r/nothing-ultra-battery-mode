package com.abaiaor.ultrasavenothing.coresystem.launcher

/**
 * Requests and releases the Android launcher ("Home") role for this app so it can act as a
 * temporary Home screen while Ultra Mode is active, blocking access to other apps from the
 * moment the user presses Home.
 *
 * Android has no public API to release a role once granted — [releaseLauncherRole] surfaces
 * guidance for the user to pick their normal launcher again rather than force-releasing it.
 */
interface LauncherRoleRepository {

    /** Launches the system flow asking the user to set this app as the Home app. */
    fun requestLauncherRole()

    /** Surfaces guidance (system Home app picker) so the user can switch back to their normal launcher. */
    fun releaseLauncherRole()
}
