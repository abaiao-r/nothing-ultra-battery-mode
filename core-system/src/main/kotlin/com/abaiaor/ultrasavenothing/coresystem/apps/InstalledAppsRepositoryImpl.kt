package com.abaiaor.ultrasavenothing.coresystem.apps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real implementation of [InstalledAppsRepository] using [PackageManager] to query all apps
 * with a launcher entry point, the same set the user would see on their normal home screen.
 */
@Singleton
class InstalledAppsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : InstalledAppsRepository {

    override fun getInstalledApps(): List<InstalledApp> {
        val packageManager = context.packageManager
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

        return packageManager.queryIntentActivities(launcherIntent, 0)
            .asSequence()
            .map { it.activityInfo.packageName }
            .distinct()
            .filterNot { it == context.packageName }
            .mapNotNull { packageName ->
                runCatching {
                    val appInfo = packageManager.getApplicationInfo(packageName, 0)
                    InstalledApp(
                        packageName = packageName,
                        label = packageManager.getApplicationLabel(appInfo).toString(),
                    )
                }.getOrNull()
            }
            .sortedBy { it.label.lowercase() }
            .toList()
    }
}
