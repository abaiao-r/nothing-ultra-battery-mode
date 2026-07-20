package com.abaiaor.ultrasavenothing.coresystem.profile

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real, no-root implementation of [SystemProfileRepository] using only public Android APIs.
 *
 * - Silent audio / no vibration: sets the ringer mode to [AudioManager.RINGER_MODE_SILENT],
 *   which suppresses both sound and vibration for calls and notifications. Requires
 *   `ACCESS_NOTIFICATION_POLICY` (Do Not Disturb access) on API 24+ to change ringer mode away
 *   from normal; without it the system silently ignores the change.
 * - Battery saver: apps cannot toggle the system battery saver directly without being a
 *   system app, so this opens the device's battery saver settings screen for the user to
 *   enable it themselves ("request", not a forced toggle).
 */
@Singleton
class SystemProfileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SystemProfileRepository {

    private val audioManager: AudioManager
        get() = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var previousRingerMode: Int? = null

    override fun applyUltraProfile() {
        previousRingerMode = audioManager.ringerMode
        audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        requestBatterySaver()
    }

    override fun revertUltraProfile() {
        audioManager.ringerMode = previousRingerMode ?: AudioManager.RINGER_MODE_NORMAL
        previousRingerMode = null
    }

    private fun requestBatterySaver() {
        val intent = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
