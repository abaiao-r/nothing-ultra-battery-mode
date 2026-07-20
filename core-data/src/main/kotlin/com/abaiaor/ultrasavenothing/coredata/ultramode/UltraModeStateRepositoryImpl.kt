package com.abaiaor.ultrasavenothing.coredata.ultramode

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** DataStore-backed implementation of [UltraModeStateRepository]. */
@Singleton
class UltraModeStateRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UltraModeStateRepository {

    override val isEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ULTRA_MODE_ENABLED_KEY] ?: false
    }

    override suspend fun setEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ULTRA_MODE_ENABLED_KEY] = enabled
        }
    }

    private companion object {
        val ULTRA_MODE_ENABLED_KEY = booleanPreferencesKey("ultra_mode_enabled")
    }
}
