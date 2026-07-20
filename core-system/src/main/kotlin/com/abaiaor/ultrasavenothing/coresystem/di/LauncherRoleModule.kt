package com.abaiaor.ultrasavenothing.coresystem.di

import com.abaiaor.ultrasavenothing.coresystem.launcher.LauncherRoleRepository
import com.abaiaor.ultrasavenothing.coresystem.launcher.LauncherRoleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Binds [LauncherRoleRepository] to its real RoleManager-based implementation for the whole app. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class LauncherRoleModule {

    @Binds
    abstract fun bindLauncherRoleRepository(
        impl: LauncherRoleRepositoryImpl,
    ): LauncherRoleRepository
}
