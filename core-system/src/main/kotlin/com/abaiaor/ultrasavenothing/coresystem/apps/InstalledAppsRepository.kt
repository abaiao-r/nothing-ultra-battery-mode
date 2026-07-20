package com.abaiaor.ultrasavenothing.coresystem.apps

/** Lists the launchable apps installed on the device, for the allowlist app picker. */
interface InstalledAppsRepository {

    /** Returns all launchable apps installed on the device, excluding this app itself. */
    fun getInstalledApps(): List<InstalledApp>
}
