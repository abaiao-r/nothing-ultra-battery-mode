package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeShellAllowlistRepository(
    initialPackages: Set<String> = emptySet(),
) : AllowlistRepository {

    private val packages = MutableStateFlow(initialPackages)

    override val allowedPackageNames: Flow<Set<String>> = packages

    override suspend fun addPackage(packageName: String) {
        packages.value = packages.value + packageName
    }

    override suspend fun removePackage(packageName: String) {
        packages.value = packages.value - packageName
    }
}
