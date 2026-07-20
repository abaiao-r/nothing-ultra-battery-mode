package com.abaiaor.ultrasavenothing.coresystem.launcher

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real implementation of [LauncherRoleRepository] using [RoleManager] to request the Home role,
 * and the system Home app picker to guide the user back to their normal launcher afterward.
 */
@Singleton
class LauncherRoleRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LauncherRoleRepository {

    private val roleManager: RoleManager
        get() = context.getSystemService(Context.ROLE_SERVICE) as RoleManager

    override fun requestLauncherRole() {
        if (!roleManager.isRoleAvailable(RoleManager.ROLE_HOME)) return
        if (roleManager.isRoleHeld(RoleManager.ROLE_HOME)) return

        val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun releaseLauncherRole() {
        val intent = Intent(Settings.ACTION_HOME_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
