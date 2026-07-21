package com.abaiaor.ultrasavenothing.coredata.root

import com.abaiaor.ultrasavenothing.coresystem.applaunch.AppLaunchRepository

class FakeAppLaunchRepository(
    private val dialerPackageName: String? = null,
    private val messagingPackageName: String? = null,
) : AppLaunchRepository {

    override fun launchApp(packageName: String) = Unit

    override fun launchPhoneApp() = Unit

    override fun launchMessagingApp() = Unit

    override fun defaultDialerPackageName(): String? = dialerPackageName

    override fun defaultMessagingPackageName(): String? = messagingPackageName
}
