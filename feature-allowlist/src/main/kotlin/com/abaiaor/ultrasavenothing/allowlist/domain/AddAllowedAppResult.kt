package com.abaiaor.ultrasavenothing.allowlist.domain

/** Result of attempting to add an app to the Ultra Mode allowlist. */
sealed interface AddAllowedAppResult {

    /** The app was added successfully. */
    data object Added : AddAllowedAppResult

    /** The app was not added because the 10-app hard cap has already been reached. */
    data object CapReached : AddAllowedAppResult
}
