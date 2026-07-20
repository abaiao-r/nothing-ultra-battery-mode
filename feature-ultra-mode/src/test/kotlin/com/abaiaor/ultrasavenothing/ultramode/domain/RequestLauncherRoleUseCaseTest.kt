package com.abaiaor.ultrasavenothing.ultramode.domain

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RequestLauncherRoleUseCaseTest {

    private lateinit var launcherRoleRepository: FakeLauncherRoleRepository
    private lateinit var useCase: RequestLauncherRoleUseCase

    @Before
    fun setUp() {
        launcherRoleRepository = FakeLauncherRoleRepository()
        useCase = RequestLauncherRoleUseCase(launcherRoleRepository)
    }

    @Test
    fun `WHEN invoked THEN launcher role is requested`() {
        useCase()

        assertEquals(1, launcherRoleRepository.requestLauncherRoleCallCount)
    }

    @Test
    fun `WHEN invoked THEN release is never called`() {
        useCase()

        assertEquals(0, launcherRoleRepository.releaseLauncherRoleCallCount)
    }
}
