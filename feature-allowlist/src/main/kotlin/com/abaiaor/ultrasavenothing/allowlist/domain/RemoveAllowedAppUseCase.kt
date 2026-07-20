package com.abaiaor.ultrasavenothing.allowlist.domain

import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository
import javax.inject.Inject

/**
 * Removes an app from the Ultra Mode allowlist. Pinned apps (Phone/SMS) can never be removed —
 * attempting to do so is rejected without touching the repository.
 */
class RemoveAllowedAppUseCase @Inject constructor(
    private val allowlistRepository: AllowlistRepository,
) {
    suspend operator fun invoke(packageName: String): RemoveAllowedAppResult {
        if (PinnedApps.isPinned(packageName)) {
            return RemoveAllowedAppResult.RejectedPinned
        }

        allowlistRepository.removePackage(packageName)
        return RemoveAllowedAppResult.Removed
    }
}
