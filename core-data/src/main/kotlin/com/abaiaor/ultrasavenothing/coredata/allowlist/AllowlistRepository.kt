package com.abaiaor.ultrasavenothing.coredata.allowlist

import kotlinx.coroutines.flow.Flow

/**
 * Persists the set of extra app package names the user has allowed to run while Ultra Mode is
 * active. Pinned apps (Phone/SMS) are never stored here — they are always implicitly allowed
 * and enforced at the UseCase layer, not in this repository.
 */
interface AllowlistRepository {

    /** Emits the current set of user-allowed extra app package names. */
    val allowedPackageNames: Flow<Set<String>>

    /** Adds [packageName] to the allowlist. */
    suspend fun addPackage(packageName: String)

    /** Removes [packageName] from the allowlist, if present. */
    suspend fun removePackage(packageName: String)
}
