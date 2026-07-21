package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coredata.root.RootProcessControlRepository
import com.abaiaor.ultrasavenothing.coredata.ultramode.UltraModeStateRepository
import com.abaiaor.ultrasavenothing.coresystem.profile.SystemProfileRepository
import javax.inject.Inject

/**
 * Disables Ultra Mode: reverts the system profile back to normal, stops the background app
 * freeze sweep (if it was running), and persists the disabled state so it survives an app
 * restart.
 */
class DisableUltraModeUseCase @Inject constructor(
    private val systemProfileRepository: SystemProfileRepository,
    private val ultraModeStateRepository: UltraModeStateRepository,
    private val rootProcessControlRepository: RootProcessControlRepository,
) {
    suspend operator fun invoke() {
        systemProfileRepository.revertUltraProfile()
        rootProcessControlRepository.stop()
        ultraModeStateRepository.setEnabled(false)
    }
}
