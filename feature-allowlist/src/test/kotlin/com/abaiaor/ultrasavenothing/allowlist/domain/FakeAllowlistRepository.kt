package com.abaiaor.ultrasavenothing.allowlist.domain

import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/** In-memory fake of [AllowlistRepository] for unit tests. */
class FakeAllowlistRepository(initialPackages: Set<String> = emptySet()) : AllowlistRepository {

    private val state = MutableStateFlow(initialPackages)

    override val allowedPackageNames: Flow<Set<String>> = state

    override suspend fun addPackage(packageName: String) {
        state.value = state.value + packageName
    }

    override suspend fun removePackage(packageName: String) {
        state.value = state.value - packageName
    }
}
