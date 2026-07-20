package com.abaiaor.ultrasavenothing.ultramode.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abaiaor.ultrasavenothing.estimation.domain.BatteryEstimate
import com.abaiaor.ultrasavenothing.estimation.domain.EstimateBatteryTimeUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.DisableUltraModeUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.EnableUltraModeUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.ObserveUltraModeStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Exposes the current Ultra Mode state and a live battery estimate to [UltraModeScreen], and
 * handles toggle intents by routing through the enable/disable UseCases — never touching a
 * Repository directly.
 */
@HiltViewModel
class UltraModeViewModel @Inject constructor(
    observeUltraModeStateUseCase: ObserveUltraModeStateUseCase,
    estimateBatteryTimeUseCase: EstimateBatteryTimeUseCase,
    private val enableUltraModeUseCase: EnableUltraModeUseCase,
    private val disableUltraModeUseCase: DisableUltraModeUseCase,
) : ViewModel() {

    val isEnabled: StateFlow<Boolean> = observeUltraModeStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false,
        )

    /**
     * Live battery estimate label (e.g. "18h 30m remaining"), recalculated whenever the
     * battery level or allowlist changes so it always reflects the current tradeoff without
     * a manual refresh.
     */
    val estimateLabel: StateFlow<String> = estimateBatteryTimeUseCase()
        .map(::formatEstimateLabel)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = "",
        )

    /** Called when the user taps the Ultra Mode toggle, requesting the new [enabled] state. */
    fun onToggle(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                enableUltraModeUseCase()
            } else {
                disableUltraModeUseCase()
            }
        }
    }

    private fun formatEstimateLabel(estimate: BatteryEstimate): String {
        val hours = estimate.remainingMinutes / MINUTES_PER_HOUR
        val minutes = estimate.remainingMinutes % MINUTES_PER_HOUR
        return if (hours > 0) {
            "${hours}h ${minutes}m remaining"
        } else {
            "${minutes}m remaining"
        }
    }

    private companion object {
        const val MINUTES_PER_HOUR = 60
    }
}
