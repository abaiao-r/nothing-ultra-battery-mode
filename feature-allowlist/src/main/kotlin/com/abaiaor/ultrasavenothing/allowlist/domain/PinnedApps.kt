package com.abaiaor.ultrasavenothing.allowlist.domain

/**
 * Package name identifiers for apps that are always allowed while Ultra Mode is active and can
 * never be removed by the user (Phone and Messages). These are never persisted in
 * [com.abaiaor.ultrasavenothing.coredata.allowlist.AllowlistRepository] — they are implicitly
 * present and enforced here.
 */
object PinnedApps {
    const val PHONE = "pinned.phone"
    const val SMS = "pinned.sms"

    val ALL: Set<String> = setOf(PHONE, SMS)

    fun isPinned(packageName: String): Boolean = packageName in ALL
}
