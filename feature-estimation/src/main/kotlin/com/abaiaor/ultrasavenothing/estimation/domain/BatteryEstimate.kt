package com.abaiaor.ultrasavenothing.estimation.domain

/**
 * A point-in-time estimate of how long the battery will last under the current Ultra Mode
 * conditions.
 *
 * @property batteryLevelPercent the battery level this estimate was calculated from, 0-100.
 * @property allowedAppCount how many extra apps (pinned apps excluded) counted against the
 *   estimate at calculation time.
 * @property remainingMinutes estimated minutes of battery life remaining, never negative.
 */
data class BatteryEstimate(
    val batteryLevelPercent: Int,
    val allowedAppCount: Int,
    val remainingMinutes: Long,
)
