package com.abaiaor.ultrasavenothing.coredata.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.abaiaor.ultrasavenothing.coredata.ultramode.UltraModeStateRepository
import com.abaiaor.ultrasavenothing.coredata.ultramode.UltraModeStateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.ultraSaveNothingDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "ultra_save_nothing_preferences",
)

/** Provides the shared [DataStore] instance backing all `:core-data` repositories. */
@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.ultraSaveNothingDataStore
}

/** Binds [UltraModeStateRepository] to its DataStore-backed implementation. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class UltraModeStateModule {

    @Binds
    abstract fun bindUltraModeStateRepository(
        impl: UltraModeStateRepositoryImpl,
    ): UltraModeStateRepository
}
