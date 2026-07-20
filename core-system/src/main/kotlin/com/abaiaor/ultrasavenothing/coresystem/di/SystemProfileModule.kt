package com.abaiaor.ultrasavenothing.coresystem.di

import com.abaiaor.ultrasavenothing.coresystem.profile.SystemProfileRepository
import com.abaiaor.ultrasavenothing.coresystem.profile.SystemProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Binds [SystemProfileRepository] to its real no-root implementation for the whole app. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class SystemProfileModule {

    @Binds
    abstract fun bindSystemProfileRepository(
        impl: SystemProfileRepositoryImpl,
    ): SystemProfileRepository
}
