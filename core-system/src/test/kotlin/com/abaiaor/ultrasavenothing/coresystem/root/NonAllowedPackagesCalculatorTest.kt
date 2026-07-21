package com.abaiaor.ultrasavenothing.coresystem.root

import org.junit.Assert.assertEquals
import org.junit.Test

class NonAllowedPackagesCalculatorTest {

    @Test
    fun `WHEN some apps are allowed THEN only non-allowed non-protected apps are returned`() {
        val result = NonAllowedPackagesCalculator.packagesToForceStop(
            installedPackageNames = setOf("com.a", "com.b", "com.c"),
            allowedPackageNames = setOf("com.a"),
            protectedPackageNames = setOf("com.b"),
        )

        assertEquals(setOf("com.c"), result)
    }

    @Test
    fun `WHEN all apps are allowed THEN nothing is returned`() {
        val result = NonAllowedPackagesCalculator.packagesToForceStop(
            installedPackageNames = setOf("com.a", "com.b"),
            allowedPackageNames = setOf("com.a", "com.b"),
            protectedPackageNames = emptySet(),
        )

        assertEquals(emptySet<String>(), result)
    }

    @Test
    fun `WHEN a package is both allowed and protected THEN it is still excluded`() {
        val result = NonAllowedPackagesCalculator.packagesToForceStop(
            installedPackageNames = setOf("com.a", "com.b"),
            allowedPackageNames = setOf("com.a"),
            protectedPackageNames = setOf("com.a"),
        )

        assertEquals(setOf("com.b"), result)
    }

    @Test
    fun `WHEN no apps are installed THEN nothing is returned`() {
        val result = NonAllowedPackagesCalculator.packagesToForceStop(
            installedPackageNames = emptySet(),
            allowedPackageNames = setOf("com.a"),
            protectedPackageNames = setOf("com.b"),
        )

        assertEquals(emptySet<String>(), result)
    }
}
