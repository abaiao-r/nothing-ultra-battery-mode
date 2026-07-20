package com.abaiaor.ultrasavenothing.ultramode.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EnableUltraModeUseCaseTest {

    private lateinit var systemProfileRepository: FakeSystemProfileRepository
    private lateinit var ultraModeStateRepository: FakeUltraModeStateRepository
    private lateinit var useCase: EnableUltraModeUseCase

    @Before
    fun setUp() {
        systemProfileRepository = FakeSystemProfileRepository()
        ultraModeStateRepository = FakeUltraModeStateRepository(initiallyEnabled = false)
        useCase = EnableUltraModeUseCase(systemProfileRepository, ultraModeStateRepository)
    }

    @Test
    fun `WHEN invoked THEN system profile is applied`() = runTest {
        useCase()

        assertEquals(1, systemProfileRepository.applyUltraProfileCallCount)
    }

    @Test
    fun `WHEN invoked THEN enabled state is persisted as true`() = runTest {
        useCase()

        assertTrue(ultraModeStateRepository.isEnabled.first())
    }

    @Test
    fun `WHEN invoked THEN revert is never called`() = runTest {
        useCase()

        assertEquals(0, systemProfileRepository.revertUltraProfileCallCount)
    }
}
