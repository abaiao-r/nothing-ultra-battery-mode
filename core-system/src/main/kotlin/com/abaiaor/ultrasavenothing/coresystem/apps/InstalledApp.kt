package com.abaiaor.ultrasavenothing.coresystem.apps

/**
 * A launchable app installed on the device, available to be added to the Ultra Mode allowlist.
 *
 * @property packageName the app's package name, used as its unique identifier everywhere else
 *   in the app (allowlist storage, UI state keys).
 * @property label the app's user-facing display name.
 */
data class InstalledApp(
    val packageName: String,
    val label: String,
)
