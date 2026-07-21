package com.abaiaor.ultrasavenothing.ultramode.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class DisableUltraModeUseCaseTest {

    private lateinit var systemProfileRepository: FakeSystemProfileRepository
    private lateinit var ultraModeStateRepository: FakeUltraModeStateRepository
    private lateinit var rootProcessControlRepository: FakeRootProcessControlRepository
    private lateinit var useCase: DisableUltraModeUseCase

    @Before
    fun setUp() {
        systemProfileRepository = FakeSystemProfileRepository()
        ultraModeStateRepository = FakeUltraModeStateRepository(initiallyEnabled = true)
        rootProcessControlRepository = FakeRootProcessControlRepository()
        useCase = DisableUltraModeUseCase(
            systemProfileRepository,
            ultraModeStateRepository,
            rootProcessControlRepository,
        )
    }

    @Test
    fun `WHEN invoked THEN system profile is reverted`() = runTest {
        useCase()

        assertEquals(1, systemProfileRepository.revertUltraProfileCallCount)
    }

    @Test
    fun `WHEN invoked THEN enabled state is persisted as false`() = runTest {
        useCase()

        assertFalse(ultraModeStateRepository.isEnabled.first())
    }

    @Test
    fun `WHEN invoked THEN apply is never called`() = runTest {
        useCase()

        assertEquals(0, systemProfileRepository.applyUltraProfileCallCount)
    }

    @Test
    fun `WHEN invoked THEN process control sweep is stopped`() = runTest {
        useCase()

        assertEquals(1, rootProcessControlRepository.stopCallCount)
    }
}
