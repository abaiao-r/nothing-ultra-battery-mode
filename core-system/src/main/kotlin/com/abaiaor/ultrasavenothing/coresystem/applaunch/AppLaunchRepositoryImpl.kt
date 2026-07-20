package com.abaiaor.ultrasavenothing.coresystem.applaunch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import android.telecom.TelecomManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real implementation of [AppLaunchRepository] using [android.content.pm.PackageManager] to
 * launch a specific app, and the system's registered default dialer/SMS package to resolve
 * "Phone" and "Messages" without hardcoding a device-specific package name.
 */
@Singleton
class AppLaunchRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppLaunchRepository {

    override fun launchApp(packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName) ?: return
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun launchPhoneApp() {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as? TelecomManager
        val defaultDialerPackage = runCatching { telecomManager?.defaultDialerPackage }.getOrNull()

        if (defaultDialerPackage != null) {
            launchApp(defaultDialerPackage)
        } else {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun launchMessagingApp() {
        val defaultSmsPackage = runCatching { Telephony.Sms.getDefaultSmsPackage(context) }.getOrNull()

        if (defaultSmsPackage != null) {
            launchApp(defaultSmsPackage)
        } else {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_APP_MESSAGING)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
}
