package com.abaiaor.ultrasavenothing.coresystem.di

import com.abaiaor.ultrasavenothing.coresystem.battery.BatteryInfoRepository
import com.abaiaor.ultrasavenothing.coresystem.battery.BatteryInfoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Binds [BatteryInfoRepository] to its real broadcast-based implementation for the whole app. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class BatteryInfoModule {

    @Binds
    abstract fun bindBatteryInfoRepository(
        impl: BatteryInfoRepositoryImpl,
    ): BatteryInfoRepository
}
