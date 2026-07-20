package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.launcher.LauncherRoleRepository
import javax.inject.Inject

/**
 * Triggers the system flow asking the user to set this app as their Home app, so unwanted
 * apps are blocked from the moment they press Home while Ultra Mode is active.
 */
class RequestLauncherRoleUseCase @Inject constructor(
    private val launcherRoleRepository: LauncherRoleRepository,
) {
    operator fun invoke() {
        launcherRoleRepository.requestLauncherRole()
    }
}
