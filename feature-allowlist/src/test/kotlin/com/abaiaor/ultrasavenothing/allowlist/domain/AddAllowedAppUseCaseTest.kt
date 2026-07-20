package com.abaiaor.ultrasavenothing.allowlist.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AddAllowedAppUseCaseTest {

    @Test
    fun `WHEN under the cap THEN adding an app succeeds`() = runTest {
        val repository = FakeAllowlistRepository()
        val useCase = AddAllowedAppUseCase(repository)

        val result = useCase("com.example.maps")

        assertEquals(AddAllowedAppResult.Added, result)
        assertTrue("com.example.maps" in repository.allowedPackageNames.first())
    }

    @Test
    fun `WHEN allowlist already has 10 apps THEN adding an 11th is rejected`() = runTest {
        val tenApps = (1..10).map { "com.example.app$it" }.toSet()
        val repository = FakeAllowlistRepository(initialPackages = tenApps)
        val useCase = AddAllowedAppUseCase(repository)

        val result = useCase("com.example.app11")

        assertEquals(AddAllowedAppResult.CapReached, result)
        assertEquals(tenApps, repository.allowedPackageNames.first())
    }

    @Test
    fun `WHEN allowlist has 10 apps AND re-adding an already-allowed app THEN it still succeeds`() =
        runTest {
            val tenApps = (1..10).map { "com.example.app$it" }.toSet()
            val repository = FakeAllowlistRepository(initialPackages = tenApps)
            val useCase = AddAllowedAppUseCase(repository)

            val result = useCase("com.example.app1")

            assertEquals(AddAllowedAppResult.Added, result)
        }
}
