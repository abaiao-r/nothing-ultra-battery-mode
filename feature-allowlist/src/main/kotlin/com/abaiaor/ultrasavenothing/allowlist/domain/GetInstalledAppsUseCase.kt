package com.abaiaor.ultrasavenothing.allowlist.domain

import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledApp
import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledAppsRepository
import javax.inject.Inject

/** Lists the installed apps available to add to the Ultra Mode allowlist. */
class GetInstalledAppsUseCase @Inject constructor(
    private val installedAppsRepository: InstalledAppsRepository,
) {
    operator fun invoke(): List<InstalledApp> = installedAppsRepository.getInstalledApps()
}
