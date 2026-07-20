package com.abaiaor.ultrasavenothing.coresystem.profile

/**
 * Applies and reverts the device-level "Ultra Mode" system profile: silent audio, vibration
 * disabled, and Android's built-in battery saver requested. This is the only layer allowed to
 * talk to Android system services (`AudioManager`, `Vibrator`, `PowerManager`).
 *
 * Kept root-free for v1: [SystemProfileRepositoryImpl] only uses public, no-root APIs. A future
 * root-based implementation can be swapped in via Hilt without touching `:feature-*` modules.
 */
interface SystemProfileRepository {

    /** Applies the full Ultra Mode profile: silent audio, no vibration, battery saver on. */
    fun applyUltraProfile()

    /** Reverts the full Ultra Mode profile back to the device's normal state. */
    fun revertUltraProfile()
}
