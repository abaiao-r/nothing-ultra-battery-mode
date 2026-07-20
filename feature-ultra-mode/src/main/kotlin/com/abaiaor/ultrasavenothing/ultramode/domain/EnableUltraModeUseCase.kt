package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coredata.ultramode.UltraModeStateRepository
import com.abaiaor.ultrasavenothing.coresystem.profile.SystemProfileRepository
import javax.inject.Inject

/**
 * Enables Ultra Mode: applies the silent/no-vibration/battery-saver system profile and
 * persists the enabled state so it survives an app restart.
 */
class EnableUltraModeUseCase @Inject constructor(
    private val systemProfileRepository: SystemProfileRepository,
    private val ultraModeStateRepository: UltraModeStateRepository,
) {
    suspend operator fun invoke() {
        systemProfileRepository.applyUltraProfile()
        ultraModeStateRepository.setEnabled(true)
    }
}
