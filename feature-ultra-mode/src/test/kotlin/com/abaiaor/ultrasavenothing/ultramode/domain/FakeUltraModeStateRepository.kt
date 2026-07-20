package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coredata.ultramode.UltraModeStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/** In-memory fake of [UltraModeStateRepository] for unit tests. */
class FakeUltraModeStateRepository(initiallyEnabled: Boolean = false) : UltraModeStateRepository {

    private val state = MutableStateFlow(initiallyEnabled)

    override val isEnabled: Flow<Boolean> = state

    var setEnabledCallCount: Int = 0
        private set

    override suspend fun setEnabled(enabled: Boolean) {
        setEnabledCallCount++
        state.value = enabled
    }
}
