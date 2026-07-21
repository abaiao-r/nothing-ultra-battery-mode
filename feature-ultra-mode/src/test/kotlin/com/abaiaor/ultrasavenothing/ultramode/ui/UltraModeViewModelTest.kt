package com.abaiaor.ultrasavenothing.ultramode.ui

import com.abaiaor.ultrasavenothing.ultramode.domain.DisableUltraModeUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.EnableUltraModeUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.FakeRootCapabilityRepository
import com.abaiaor.ultrasavenothing.ultramode.domain.FakeRootProcessControlRepository
import com.abaiaor.ultrasavenothing.ultramode.domain.FakeSystemProfileRepository
import com.abaiaor.ultrasavenothing.ultramode.domain.FakeUltraModeAllowlistRepository
import com.abaiaor.ultrasavenothing.ultramode.domain.FakeUltraModeBatteryInfoRepository
import com.abaiaor.ultrasavenothing.ultramode.domain.FakeUltraModeStateRepository
import com.abaiaor.ultrasavenothing.ultramode.domain.ObserveUltraModeStateUseCase
import com.abaiaor.ultrasavenothing.estimation.domain.EstimateBatteryTimeUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UltraModeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var systemProfileRepository: FakeSystemProfileRepository
    private lateinit var ultraModeStateRepository: FakeUltraModeStateRepository
    private lateinit var batteryInfoRepository: FakeUltraModeBatteryInfoRepository
    private lateinit var allowlistRepository: FakeUltraModeAllowlistRepository
    private lateinit var viewModel: UltraModeViewModel

    private fun createViewModel(
        initiallyEnabled: Boolean = false,
        initialBatteryLevelPercent: Int = 100,
    ) {
        systemProfileRepository = FakeSystemProfileRepository()
        ultraModeStateRepository = FakeUltraModeStateRepository(initiallyEnabled)
        batteryInfoRepository = FakeUltraModeBatteryInfoRepository(initialBatteryLevelPercent)
        allowlistRepository = FakeUltraModeAllowlistRepository()
        viewModel = UltraModeViewModel(
            observeUltraModeStateUseCase = ObserveUltraModeStateUseCase(ultraModeStateRepository),
            estimateBatteryTimeUseCase = EstimateBatteryTimeUseCase(batteryInfoRepository, allowlistRepository),
            enableUltraModeUseCase = EnableUltraModeUseCase(
                systemProfileRepository,
                ultraModeStateRepository,
                FakeRootCapabilityRepository(isRootAvailable = false),
                FakeRootProcessControlRepository(),
            ),
            disableUltraModeUseCase = DisableUltraModeUseCase(
                systemProfileRepository,
                ultraModeStateRepository,
                FakeRootProcessControlRepository(),
            ),
        )
    }

    @Before
    fun setUp() {
        createViewModel()
    }

    @Test
    fun `WHEN initial state is disabled THEN isEnabled starts false`() = runTest {
        assertFalse(viewModel.isEnabled.value)
    }

    @Test
    fun `WHEN created with persisted enabled state THEN isEnabled starts true`() = runTest {
        createViewModel(initiallyEnabled = true)

        assertTrue(viewModel.isEnabled.value)
    }

    @Test
    fun `WHEN onToggle is called with true THEN ultra mode is enabled end to end`() = runTest {
        viewModel.onToggle(true)

        assertTrue(ultraModeStateRepository.isEnabled.first())
        assertEquals(1, systemProfileRepository.applyUltraProfileCallCount)
    }

    @Test
    fun `WHEN onToggle is called with false THEN ultra mode is disabled end to end`() = runTest {
        createViewModel(initiallyEnabled = true)

        viewModel.onToggle(false)

        assertFalse(ultraModeStateRepository.isEnabled.first())
        assertEquals(1, systemProfileRepository.revertUltraProfileCallCount)
    }

    @Test
    fun `WHEN no apps are allowed THEN estimateLabel reflects the base drain rate`() = runTest {
        assertEquals("50h 0m remaining", viewModel.estimateLabel.value)
    }

    @Test
    fun `WHEN allowlist changes THEN estimateLabel updates live without manual refresh`() = runTest {
        allowlistRepository.addPackage("com.example.one")

        assertEquals("40h 0m remaining", viewModel.estimateLabel.value)
    }

    @Test
    fun `WHEN remaining time is under an hour THEN estimateLabel omits the hours part`() = runTest {
        createViewModel(initialBatteryLevelPercent = 0)

        assertEquals("0m remaining", viewModel.estimateLabel.value)
    }
}
