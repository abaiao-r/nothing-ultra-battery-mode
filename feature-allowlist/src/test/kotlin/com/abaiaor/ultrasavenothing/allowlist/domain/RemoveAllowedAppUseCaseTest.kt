package com.abaiaor.ultrasavenothing.allowlist.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoveAllowedAppUseCaseTest {

    @Test
    fun `WHEN removing a non-pinned app THEN it is removed`() = runTest {
        val repository = FakeAllowlistRepository(initialPackages = setOf("com.example.maps"))
        val useCase = RemoveAllowedAppUseCase(repository)

        val result = useCase("com.example.maps")

        assertEquals(RemoveAllowedAppResult.Removed, result)
        assertFalse("com.example.maps" in repository.allowedPackageNames.first())
    }

    @Test
    fun `WHEN removing the pinned Phone app THEN it is rejected`() = runTest {
        val repository = FakeAllowlistRepository()
        val useCase = RemoveAllowedAppUseCase(repository)

        val result = useCase(PinnedApps.PHONE)

        assertEquals(RemoveAllowedAppResult.RejectedPinned, result)
    }

    @Test
    fun `WHEN removing the pinned SMS app THEN it is rejected`() = runTest {
        val repository = FakeAllowlistRepository()
        val useCase = RemoveAllowedAppUseCase(repository)

        val result = useCase(PinnedApps.SMS)

        assertEquals(RemoveAllowedAppResult.RejectedPinned, result)
    }

    @Test
    fun `WHEN removing a pinned app THEN repository is never modified`() = runTest {
        val repository = FakeAllowlistRepository(initialPackages = setOf("com.example.maps"))
        val useCase = RemoveAllowedAppUseCase(repository)

        useCase(PinnedApps.PHONE)

        assertTrue("com.example.maps" in repository.allowedPackageNames.first())
    }
}
