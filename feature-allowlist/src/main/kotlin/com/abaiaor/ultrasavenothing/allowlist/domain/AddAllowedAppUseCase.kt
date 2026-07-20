package com.abaiaor.ultrasavenothing.allowlist.domain

import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Adds an app to the Ultra Mode allowlist, enforcing the hard cap of [MAX_ALLOWED_APPS] extra
 * apps. Pinned apps (Phone/SMS) are always implicitly allowed and never go through this
 * UseCase or count against the cap.
 */
class AddAllowedAppUseCase @Inject constructor(
    private val allowlistRepository: AllowlistRepository,
) {
    suspend operator fun invoke(packageName: String): AddAllowedAppResult {
        val current = allowlistRepository.allowedPackageNames.first()

        if (packageName !in current && current.size >= MAX_ALLOWED_APPS) {
            return AddAllowedAppResult.CapReached
        }

        allowlistRepository.addPackage(packageName)
        return AddAllowedAppResult.Added
    }

    companion object {
        /** Hard cap on the number of extra (non-pinned) apps allowed while Ultra Mode is on. */
        const val MAX_ALLOWED_APPS = 10
    }
}
