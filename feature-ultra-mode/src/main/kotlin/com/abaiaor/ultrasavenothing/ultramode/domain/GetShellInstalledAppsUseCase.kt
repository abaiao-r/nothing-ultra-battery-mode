package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledApp
import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledAppsRepository
import javax.inject.Inject

/** Lists the installed apps, for resolving display labels of allowed apps shown in the shell. */
class GetShellInstalledAppsUseCase @Inject constructor(
    private val installedAppsRepository: InstalledAppsRepository,
) {
    operator fun invoke(): List<InstalledApp> = installedAppsRepository.getInstalledApps()
}
