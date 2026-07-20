package com.abaiaor.ultrasavenothing.allowlist.domain

/** Result of attempting to remove an app from the Ultra Mode allowlist. */
sealed interface RemoveAllowedAppResult {

    /** The app was removed successfully. */
    data object Removed : RemoveAllowedAppResult

    /** The app was not removed because it is a pinned app (Phone/SMS) and can never be removed. */
    data object RejectedPinned : RemoveAllowedAppResult
}
