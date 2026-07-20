package com.abaiaor.ultrasavenothing.coresystem.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Real implementation of [BatteryInfoRepository] using the sticky [Intent.ACTION_BATTERY_CHANGED]
 * broadcast, which requires no permission and always has a last-known value available
 * immediately on registration.
 */
@Singleton
class BatteryInfoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : BatteryInfoRepository {

    override val batteryLevelPercent: Flow<Int> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                trySend(levelPercentFrom(intent))
            }
        }
        val sticky = context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        sticky?.let { trySend(levelPercentFrom(it)) }

        awaitClose { context.unregisterReceiver(receiver) }
    }.distinctUntilChanged()

    private fun levelPercentFrom(intent: Intent): Int {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        if (level < 0 || scale <= 0) return 0
        return (level * 100) / scale
    }
}
