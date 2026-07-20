package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledApp
import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledAppsRepository

class FakeInstalledAppsRepository(
    private val apps: List<InstalledApp> = emptyList(),
) : InstalledAppsRepository {

    override fun getInstalledApps(): List<InstalledApp> = apps
}
