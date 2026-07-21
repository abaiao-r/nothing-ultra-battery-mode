package com.abaiaor.ultrasavenothing.coresystem.root

/**
 * Reports whether this device currently has root (`su`) access granted to this app, so
 * root-powered Ultra Mode features can be offered when available and the app can gracefully
 * fall back to non-root behavior otherwise.
 */
interface RootCapabilityRepository {

    /**
     * Returns true only if root is actually available and this app has been granted superuser
     * access (e.g. approved once in Magisk's superuser prompt). Safe to call repeatedly; never
     * throws.
     */
    suspend fun isRootAvailable(): Boolean
}
