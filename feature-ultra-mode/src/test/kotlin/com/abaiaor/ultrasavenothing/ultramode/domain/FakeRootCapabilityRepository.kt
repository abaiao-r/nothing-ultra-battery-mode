package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.root.RootCapabilityRepository

class FakeRootCapabilityRepository(
    private val isRootAvailable: Boolean = false,
) : RootCapabilityRepository {

    override suspend fun isRootAvailable(): Boolean = isRootAvailable
}
