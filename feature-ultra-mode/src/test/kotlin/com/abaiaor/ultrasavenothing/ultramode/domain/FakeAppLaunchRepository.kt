package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.applaunch.AppLaunchRepository

class FakeAppLaunchRepository(
    private val dialerPackageName: String? = null,
    private val messagingPackageName: String? = null,
) : AppLaunchRepository {

    var launchedPackageName: String? = null
        private set
    var phoneAppLaunchCount: Int = 0
        private set
    var messagingAppLaunchCount: Int = 0
        private set

    override fun launchApp(packageName: String) {
        launchedPackageName = packageName
    }

    override fun launchPhoneApp() {
        phoneAppLaunchCount++
    }

    override fun launchMessagingApp() {
        messagingAppLaunchCount++
    }

    override fun defaultDialerPackageName(): String? = dialerPackageName

    override fun defaultMessagingPackageName(): String? = messagingPackageName
}
