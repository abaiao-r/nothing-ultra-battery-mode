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
    private lateinit var rootCapabilityRepository: FakeRootCapabilityRepository
    private lateinit var rootProcessControlRepository: FakeRootProcessControlRepository
    private lateinit var useCase: EnableUltraModeUseCase

    @Before
    fun setUp() {
        systemProfileRepository = FakeSystemProfileRepository()
        ultraModeStateRepository = FakeUltraModeStateRepository(initiallyEnabled = false)
        rootCapabilityRepository = FakeRootCapabilityRepository(isRootAvailable = true)
        rootProcessControlRepository = FakeRootProcessControlRepository()
        useCase = EnableUltraModeUseCase(
            systemProfileRepository,
            ultraModeStateRepository,
            rootCapabilityRepository,
            rootProcessControlRepository,
        )
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

    @Test
    fun `WHEN root is available THEN process control sweep is started`() = runTest {
        useCase()

        assertEquals(1, rootProcessControlRepository.startCallCount)
    }

    @Test
    fun `WHEN root is not available THEN process control sweep is not started`() = runTest {
        rootCapabilityRepository = FakeRootCapabilityRepository(isRootAvailable = false)
        useCase = EnableUltraModeUseCase(
            systemProfileRepository,
            ultraModeStateRepository,
            rootCapabilityRepository,
            rootProcessControlRepository,
        )

        useCase()

        assertEquals(0, rootProcessControlRepository.startCallCount)
    }
}
