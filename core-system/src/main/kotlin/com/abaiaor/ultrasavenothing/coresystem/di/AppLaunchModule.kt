package com.abaiaor.ultrasavenothing.coresystem.di

import com.abaiaor.ultrasavenothing.coresystem.applaunch.AppLaunchRepository
import com.abaiaor.ultrasavenothing.coresystem.applaunch.AppLaunchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Binds [AppLaunchRepository] to its real PackageManager/TelecomManager-based implementation. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppLaunchModule {

    @Binds
    abstract fun bindAppLaunchRepository(
        impl: AppLaunchRepositoryImpl,
    ): AppLaunchRepository
}
