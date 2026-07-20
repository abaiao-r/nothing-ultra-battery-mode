package com.abaiaor.ultrasavenothing.ultramode.domain

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ReleaseLauncherRoleUseCaseTest {

    private lateinit var launcherRoleRepository: FakeLauncherRoleRepository
    private lateinit var useCase: ReleaseLauncherRoleUseCase

    @Before
    fun setUp() {
        launcherRoleRepository = FakeLauncherRoleRepository()
        useCase = ReleaseLauncherRoleUseCase(launcherRoleRepository)
    }

    @Test
    fun `WHEN invoked THEN launcher role is released`() {
        useCase()

        assertEquals(1, launcherRoleRepository.releaseLauncherRoleCallCount)
    }

    @Test
    fun `WHEN invoked THEN request is never called`() {
        useCase()

        assertEquals(0, launcherRoleRepository.requestLauncherRoleCallCount)
    }
}
