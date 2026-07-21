package com.abaiaor.ultrasavenothing.coredata.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository
import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepositoryImpl
import com.abaiaor.ultrasavenothing.coredata.root.RootProcessControlRepository
import com.abaiaor.ultrasavenothing.coredata.root.RootProcessControlRepositoryImpl
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

/** Binds [AllowlistRepository] to its DataStore-backed implementation. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class AllowlistModule {

    @Binds
    abstract fun bindAllowlistRepository(
        impl: AllowlistRepositoryImpl,
    ): AllowlistRepository
}

/** Binds [RootProcessControlRepository] to its real root-shell-backed implementation. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class RootProcessControlModule {

    @Binds
    abstract fun bindRootProcessControlRepository(
        impl: RootProcessControlRepositoryImpl,
    ): RootProcessControlRepository
}
