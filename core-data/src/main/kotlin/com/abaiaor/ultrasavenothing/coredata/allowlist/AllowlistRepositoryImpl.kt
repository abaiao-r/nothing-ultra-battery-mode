package com.abaiaor.ultrasavenothing.coredata.allowlist

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** DataStore-backed implementation of [AllowlistRepository]. */
@Singleton
class AllowlistRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AllowlistRepository {

    override val allowedPackageNames: Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[ALLOWED_PACKAGES_KEY] ?: emptySet()
    }

    override suspend fun addPackage(packageName: String) {
        dataStore.edit { preferences ->
            val current = preferences[ALLOWED_PACKAGES_KEY] ?: emptySet()
            preferences[ALLOWED_PACKAGES_KEY] = current + packageName
        }
    }

    override suspend fun removePackage(packageName: String) {
        dataStore.edit { preferences ->
            val current = preferences[ALLOWED_PACKAGES_KEY] ?: emptySet()
            preferences[ALLOWED_PACKAGES_KEY] = current - packageName
        }
    }

    private companion object {
        val ALLOWED_PACKAGES_KEY = stringSetPreferencesKey("allowlist_package_names")
    }
}
