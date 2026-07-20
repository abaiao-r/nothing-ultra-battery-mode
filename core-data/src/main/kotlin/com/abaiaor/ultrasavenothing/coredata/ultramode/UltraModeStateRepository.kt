package com.abaiaor.ultrasavenothing.coredata.ultramode

import kotlinx.coroutines.flow.Flow

/** Persists whether Ultra Mode is currently enabled, surviving app restarts. */
interface UltraModeStateRepository {

    /** Emits the current Ultra Mode enabled state, starting with the persisted value. */
    val isEnabled: Flow<Boolean>

    /** Persists the new Ultra Mode enabled state. */
    suspend fun setEnabled(enabled: Boolean)
}
