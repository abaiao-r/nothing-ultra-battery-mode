package com.abaiaor.ultrasavenothing.coresystem.battery

import kotlinx.coroutines.flow.Flow

/**
 * Exposes the device's current battery level so higher layers (e.g. the battery time estimate)
 * can react to it without talking to Android system services directly.
 */
interface BatteryInfoRepository {

    /** Current battery level as a percentage, 0-100, updated whenever the system reports it. */
    val batteryLevelPercent: Flow<Int>
}
