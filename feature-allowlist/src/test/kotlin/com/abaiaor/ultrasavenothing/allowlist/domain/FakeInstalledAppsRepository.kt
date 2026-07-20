package com.abaiaor.ultrasavenothing.allowlist.domain

import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledApp
import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledAppsRepository

/** In-memory fake of [InstalledAppsRepository] for unit tests. */
class FakeInstalledAppsRepository(
    private val apps: List<InstalledApp> = emptyList(),
) : InstalledAppsRepository {

    override fun getInstalledApps(): List<InstalledApp> = apps
}
