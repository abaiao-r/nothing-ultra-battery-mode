package com.abaiaor.ultrasavenothing.coresystem.di

import com.abaiaor.ultrasavenothing.coresystem.root.LibsuRootShellExecutor
import com.abaiaor.ultrasavenothing.coresystem.root.RootShellExecutor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Binds [RootShellExecutor] to its real libsu-backed implementation. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class RootShellModule {

    @Binds
    abstract fun bindRootShellExecutor(
        impl: LibsuRootShellExecutor,
    ): RootShellExecutor
}
