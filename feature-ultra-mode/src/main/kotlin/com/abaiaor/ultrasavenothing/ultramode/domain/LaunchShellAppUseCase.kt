package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.applaunch.AppLaunchRepository
import javax.inject.Inject

/**
 * Launches the app tapped in the minimal Ultra Mode shell: the device's default Phone/Messaging
 * app for the pinned entries, or a specific allowed app by package name otherwise.
 */
class LaunchShellAppUseCase @Inject constructor(
    private val appLaunchRepository: AppLaunchRepository,
) {
    operator fun invoke(packageName: String) {
        when (packageName) {
            ShellPinnedEntries.PHONE -> appLaunchRepository.launchPhoneApp()
            ShellPinnedEntries.SMS -> appLaunchRepository.launchMessagingApp()
            else -> appLaunchRepository.launchApp(packageName)
        }
    }
}
