package com.abaiaor.ultrasavenothing.coredata.root

import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledApp
import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledAppsRepository

class FakeInstalledAppsRepository(
    private val installedApps: List<InstalledApp> = emptyList(),
) : InstalledAppsRepository {

    override fun getInstalledApps(): List<InstalledApp> = installedApps
}
