package com.abaiaor.ultrasavenothing.ultramode.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abaiaor.ultrasavenothing.ultramode.domain.DisableUltraModeUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.EnableUltraModeUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.ObserveUltraModeStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Exposes the current Ultra Mode state to [UltraModeScreen] and handles toggle intents by
 * routing through the enable/disable UseCases — never touching a Repository directly.
 */
@HiltViewModel
class UltraModeViewModel @Inject constructor(
    observeUltraModeStateUseCase: ObserveUltraModeStateUseCase,
    private val enableUltraModeUseCase: EnableUltraModeUseCase,
    private val disableUltraModeUseCase: DisableUltraModeUseCase,
) : ViewModel() {

    val isEnabled: StateFlow<Boolean> = observeUltraModeStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false,
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
}
