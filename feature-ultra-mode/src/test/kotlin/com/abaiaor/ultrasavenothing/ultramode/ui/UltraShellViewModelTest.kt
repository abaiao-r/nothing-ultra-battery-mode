package com.abaiaor.ultrasavenothing.ultramode.ui

import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledApp
import com.abaiaor.ultrasavenothing.ultramode.domain.FakeAppLaunchRepository
import com.abaiaor.ultrasavenothing.ultramode.domain.FakeInstalledAppsRepository
import com.abaiaor.ultrasavenothing.ultramode.domain.FakeShellAllowlistRepository
import com.abaiaor.ultrasavenothing.ultramode.domain.GetShellInstalledAppsUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.LaunchShellAppUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.ObserveShellAllowlistUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.ShellPinnedEntries
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UltraShellViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        allowedPackages: Set<String> = emptySet(),
        installedApps: List<InstalledApp> = emptyList(),
        appLaunchRepository: FakeAppLaunchRepository = FakeAppLaunchRepository(),
    ): Pair<UltraShellViewModel, FakeAppLaunchRepository> {
        val viewModel = UltraShellViewModel(
            observeShellAllowlistUseCase = ObserveShellAllowlistUseCase(
                FakeShellAllowlistRepository(allowedPackages),
            ),
            getShellInstalledAppsUseCase = GetShellInstalledAppsUseCase(
                FakeInstalledAppsRepository(installedApps),
            ),
            launchShellAppUseCase = LaunchShellAppUseCase(appLaunchRepository),
        )
        return viewModel to appLaunchRepository
    }

    @Test
    fun `WHEN no apps are allowed THEN only pinned Phone and Messages entries are shown`() = runTest {
        val (viewModel, _) = createViewModel()

        val entries = viewModel.entries.value

        assertEquals(2, entries.size)
        assertTrue(entries.all { it.isPinned })
        assertEquals(ShellPinnedEntries.PHONE, entries[0].packageName)
        assertEquals(ShellPinnedEntries.SMS, entries[1].packageName)
    }

    @Test
    fun `WHEN apps are allowed THEN they appear after the pinned entries with resolved labels`() = runTest {
        val (viewModel, _) = createViewModel(
            allowedPackages = setOf("com.maps.app", "com.music.app"),
            installedApps = listOf(
                InstalledApp(packageName = "com.maps.app", label = "Maps"),
                InstalledApp(packageName = "com.music.app", label = "Music"),
            ),
        )

        val entries = viewModel.entries.value

        assertEquals(4, entries.size)
        assertEquals(listOf("Phone", "Messages", "Maps", "Music"), entries.map { it.label })
        assertTrue(entries.drop(2).none { it.isPinned })
    }

    @Test
    fun `WHEN an allowed app has no installed-apps match THEN its package name is used as label`() = runTest {
        val (viewModel, _) = createViewModel(allowedPackages = setOf("com.unknown.app"))

        val entries = viewModel.entries.value

        assertEquals("com.unknown.app", entries.last().label)
    }

    @Test
    fun `WHEN Phone entry is clicked THEN the phone app is launched`() = runTest {
        val (viewModel, appLaunchRepository) = createViewModel()

        viewModel.onAppClick(ShellPinnedEntries.PHONE)

        assertEquals(1, appLaunchRepository.phoneAppLaunchCount)
    }

    @Test
    fun `WHEN Messages entry is clicked THEN the messaging app is launched`() = runTest {
        val (viewModel, appLaunchRepository) = createViewModel()

        viewModel.onAppClick(ShellPinnedEntries.SMS)

        assertEquals(1, appLaunchRepository.messagingAppLaunchCount)
    }

    @Test
    fun `WHEN an allowed app entry is clicked THEN that specific app is launched`() = runTest {
        val (viewModel, appLaunchRepository) = createViewModel(
            allowedPackages = setOf("com.maps.app"),
        )

        viewModel.onAppClick("com.maps.app")

        assertEquals("com.maps.app", appLaunchRepository.launchedPackageName)
    }
}
