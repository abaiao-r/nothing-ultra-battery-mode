package com.abaiaor.ultrasavenothing.coresystem.root

/**
 * Pure, unit-testable business rule for which installed apps should be force-stopped while
 * Ultra Mode is active: every installed app that is neither on the live allowlist nor in the
 * always-protected set (this app itself, the default dialer, the default messaging app).
 */
object NonAllowedPackagesCalculator {

    fun packagesToForceStop(
        installedPackageNames: Set<String>,
        allowedPackageNames: Set<String>,
        protectedPackageNames: Set<String>,
    ): Set<String> = installedPackageNames - allowedPackageNames - protectedPackageNames
}
