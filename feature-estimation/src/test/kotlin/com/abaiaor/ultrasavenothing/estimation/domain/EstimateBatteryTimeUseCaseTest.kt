package com.abaiaor.ultrasavenothing.estimation.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EstimateBatteryTimeUseCaseTest {

    private lateinit var batteryInfoRepository: FakeBatteryInfoRepository
    private lateinit var allowlistRepository: FakeAllowlistRepository
    private lateinit var useCase: EstimateBatteryTimeUseCase

    @Before
    fun setUp() {
        batteryInfoRepository = FakeBatteryInfoRepository(initialLevelPercent = 100)
        allowlistRepository = FakeAllowlistRepository()
        useCase = EstimateBatteryTimeUseCase(batteryInfoRepository, allowlistRepository)
    }

    @Test
    fun `WHEN no allowed apps THEN estimate uses only the base drain rate`() = runTest {
        val estimate = useCase().first()

        assertEquals(0, estimate.allowedAppCount)
        assertEquals(100, estimate.batteryLevelPercent)
        assertEquals(3000L, estimate.remainingMinutes) // 100 / 2.0 * 60
    }

    @Test
    fun `WHEN one app allowed THEN estimate decreases from the zero-app baseline`() = runTest {
        allowlistRepository.addPackage("com.example.one")

        val estimate = useCase().first()

        assertEquals(1, estimate.allowedAppCount)
        assertEquals(2400L, estimate.remainingMinutes) // 100 / 2.5 * 60
    }

    @Test
    fun `WHEN five apps allowed THEN estimate is lower than with one app`() = runTest {
        repeat(5) { allowlistRepository.addPackage("com.example.$it") }

        val estimate = useCase().first()

        assertEquals(5, estimate.allowedAppCount)
        assertEquals(1333L, estimate.remainingMinutes) // 100 / 4.5 * 60, floored
    }

    @Test
    fun `WHEN ten apps allowed THEN estimate is lower than with five apps`() = runTest {
        repeat(10) { allowlistRepository.addPackage("com.example.$it") }

        val estimate = useCase().first()

        assertEquals(10, estimate.allowedAppCount)
        assertEquals(857L, estimate.remainingMinutes) // 100 / 7.0 * 60, floored
    }

    @Test
    fun `WHEN allowlist grows THEN estimate strictly decreases as app count increases`() = runTest {
        val zeroApps = useCase().first().remainingMinutes
        allowlistRepository.addPackage("com.example.a")
        val oneApp = useCase().first().remainingMinutes
        repeat(4) { allowlistRepository.addPackage("com.example.b$it") }
        val fiveApps = useCase().first().remainingMinutes
        repeat(5) { allowlistRepository.addPackage("com.example.c$it") }
        val tenApps = useCase().first().remainingMinutes

        assertTrue(zeroApps > oneApp)
        assertTrue(oneApp > fiveApps)
        assertTrue(fiveApps > tenApps)
    }

    @Test
    fun `WHEN battery level is zero THEN remaining minutes is zero not negative`() = runTest {
        batteryInfoRepository.setLevelPercent(0)

        val estimate = useCase().first()

        assertEquals(0L, estimate.remainingMinutes)
    }
}
