package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coredata.ultramode.UltraModeStateRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/** Observes whether Ultra Mode is currently enabled, for display in the UI layer. */
class ObserveUltraModeStateUseCase @Inject constructor(
    private val ultraModeStateRepository: UltraModeStateRepository,
) {
    operator fun invoke(): Flow<Boolean> = ultraModeStateRepository.isEnabled
}
