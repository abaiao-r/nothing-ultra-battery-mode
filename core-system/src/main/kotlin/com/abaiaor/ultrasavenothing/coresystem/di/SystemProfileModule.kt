package com.abaiaor.ultrasavenothing.coresystem.di

import com.abaiaor.ultrasavenothing.coresystem.profile.SystemProfileRepository
import com.abaiaor.ultrasavenothing.coresystem.profile.SystemProfileRepositoryImpl
import com.abaiaor.ultrasavenothing.coresystem.root.RootCapabilityRepository
import com.abaiaor.ultrasavenothing.coresystem.root.RootSystemProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking

/**
 * Selects, once per process, which [SystemProfileRepository] implementation the whole app
 * uses: the real root-powered one ([RootSystemProfileRepositoryImpl]) when this device actually
 * has root access, or the existing no-root implementation ([SystemProfileRepositoryImpl])
 * otherwise. `:feature-ultra-mode` depends only on the [SystemProfileRepository] interface and
 * never needs to know which one is active.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object SystemProfileModule {

    @Provides
    @Singleton
    fun provideSystemProfileRepository(
        rootCapabilityRepository: RootCapabilityRepository,
        rootImpl: RootSystemProfileRepositoryImpl,
        nonRootImpl: SystemProfileRepositoryImpl,
    ): SystemProfileRepository {
        val isRootAvailable = runBlocking { rootCapabilityRepository.isRootAvailable() }
        return if (isRootAvailable) rootImpl else nonRootImpl
    }
}
