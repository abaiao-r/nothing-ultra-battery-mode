package com.abaiaor.ultrasavenothing.coresystem.di

import com.abaiaor.ultrasavenothing.coresystem.root.RootCapabilityRepository
import com.abaiaor.ultrasavenothing.coresystem.root.RootCapabilityRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Binds [RootCapabilityRepository] to its real libsu-backed implementation. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class RootCapabilityModule {

    @Binds
    abstract fun bindRootCapabilityRepository(
        impl: RootCapabilityRepositoryImpl,
    ): RootCapabilityRepository
}
