package com.abaiaor.ultrasavenothing.coredata.root

import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAllowlistRepository(
    initiallyAllowed: Set<String> = emptySet(),
) : AllowlistRepository {

    private val allowedPackageNamesFlow = MutableStateFlow(initiallyAllowed)

    override val allowedPackageNames = allowedPackageNamesFlow

    override suspend fun addPackage(packageName: String) {
        allowedPackageNamesFlow.value = allowedPackageNamesFlow.value + packageName
    }

    override suspend fun removePackage(packageName: String) {
        allowedPackageNamesFlow.value = allowedPackageNamesFlow.value - packageName
    }
}
