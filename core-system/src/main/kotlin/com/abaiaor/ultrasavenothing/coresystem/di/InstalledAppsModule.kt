package com.abaiaor.ultrasavenothing.coresystem.di

import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledAppsRepository
import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledAppsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Binds [InstalledAppsRepository] to its real PackageManager-based implementation. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class InstalledAppsModule {

    @Binds
    abstract fun bindInstalledAppsRepository(
        impl: InstalledAppsRepositoryImpl,
    ): InstalledAppsRepository
}
