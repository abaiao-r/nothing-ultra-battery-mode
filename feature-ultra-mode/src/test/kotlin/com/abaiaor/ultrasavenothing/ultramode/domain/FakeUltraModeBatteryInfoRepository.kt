package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.battery.BatteryInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUltraModeBatteryInfoRepository(
    initialLevelPercent: Int = 100,
) : BatteryInfoRepository {

    private val levelPercent = MutableStateFlow(initialLevelPercent)

    override val batteryLevelPercent: Flow<Int> = levelPercent

    fun setLevelPercent(value: Int) {
        levelPercent.value = value
    }
}
