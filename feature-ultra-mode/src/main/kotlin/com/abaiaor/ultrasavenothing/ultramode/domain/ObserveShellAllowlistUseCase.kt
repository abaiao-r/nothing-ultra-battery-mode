package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/** Observes the current set of extra (non-pinned) allowed app package names for the shell. */
class ObserveShellAllowlistUseCase @Inject constructor(
    private val allowlistRepository: AllowlistRepository,
) {
    operator fun invoke(): Flow<Set<String>> = allowlistRepository.allowedPackageNames
}
