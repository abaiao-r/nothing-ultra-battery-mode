package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coredata.root.RootProcessControlRepository
import com.abaiaor.ultrasavenothing.coredata.ultramode.UltraModeStateRepository
import com.abaiaor.ultrasavenothing.coresystem.profile.SystemProfileRepository
import com.abaiaor.ultrasavenothing.coresystem.root.RootCapabilityRepository
import javax.inject.Inject

/**
 * Enables Ultra Mode: applies the system profile (real root-powered profile on a rooted
 * device, best-effort no-root profile otherwise), starts the root-powered background app
 * freeze sweep when root is available, and persists the enabled state so it survives an app
 * restart.
 */
class EnableUltraModeUseCase @Inject constructor(
    private val systemProfileRepository: SystemProfileRepository,
    private val ultraModeStateRepository: UltraModeStateRepository,
    private val rootCapabilityRepository: RootCapabilityRepository,
    private val rootProcessControlRepository: RootProcessControlRepository,
) {
    suspend operator fun invoke() {
        systemProfileRepository.applyUltraProfile()
        if (rootCapabilityRepository.isRootAvailable()) {
            rootProcessControlRepository.start()
        }
        ultraModeStateRepository.setEnabled(true)
    }
}
