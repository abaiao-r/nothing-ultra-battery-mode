package com.abaiaor.ultrasavenothing.estimation.domain

import com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository
import com.abaiaor.ultrasavenothing.coresystem.battery.BatteryInfoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Continuously estimates remaining battery time under Ultra Mode, recalculating whenever the
 * battery level or the allowlist changes.
 *
 * Model: a fixed baseline drain rate for the "silent, minimal apps" Ultra Mode state, plus a
 * fixed penalty per extra allowed app (each extra app running in the background is assumed to
 * add a constant drain). This keeps the estimate simple, deterministic, and instantly reactive
 * to allowlist edits, at the cost of not measuring real per-app power usage (out of scope for
 * v1 — see architecture.instructions.md).
 */
class EstimateBatteryTimeUseCase @Inject constructor(
    private val batteryInfoRepository: BatteryInfoRepository,
    private val allowlistRepository: AllowlistRepository,
) {

    operator fun invoke(): Flow<BatteryEstimate> =
        combine(
            batteryInfoRepository.batteryLevelPercent,
            allowlistRepository.allowedPackageNames,
        ) { batteryLevelPercent, allowedPackageNames ->
            calculate(batteryLevelPercent, allowedPackageNames.size)
        }

    private fun calculate(batteryLevelPercent: Int, allowedAppCount: Int): BatteryEstimate {
        val drainRatePercentPerHour =
            BASE_DRAIN_RATE_PERCENT_PER_HOUR + (allowedAppCount * PER_APP_DRAIN_PENALTY_PERCENT_PER_HOUR)
        val hoursRemaining = batteryLevelPercent / drainRatePercentPerHour
        val remainingMinutes = (hoursRemaining * MINUTES_PER_HOUR)
            .toLong()
            .coerceAtLeast(0L)
        return BatteryEstimate(
            batteryLevelPercent = batteryLevelPercent,
            allowedAppCount = allowedAppCount,
            remainingMinutes = remainingMinutes,
        )
    }

    private companion object {
        const val BASE_DRAIN_RATE_PERCENT_PER_HOUR = 2.0
        const val PER_APP_DRAIN_PENALTY_PERCENT_PER_HOUR = 0.5
        const val MINUTES_PER_HOUR = 60
    }
}
