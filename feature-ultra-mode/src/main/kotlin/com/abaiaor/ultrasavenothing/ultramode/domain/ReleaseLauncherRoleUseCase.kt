package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.launcher.LauncherRoleRepository
import javax.inject.Inject

/**
 * Surfaces guidance for the user to switch back to their normal launcher once Ultra Mode is
 * disabled, since Android has no public API to release a role once granted.
 */
class ReleaseLauncherRoleUseCase @Inject constructor(
    private val launcherRoleRepository: LauncherRoleRepository,
) {
    operator fun invoke() {
        launcherRoleRepository.releaseLauncherRole()
    }
}
