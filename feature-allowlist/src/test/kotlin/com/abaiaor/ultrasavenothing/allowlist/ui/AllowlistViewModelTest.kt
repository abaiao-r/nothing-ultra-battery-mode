package com.abaiaor.ultrasavenothing.allowlist.ui

import com.abaiaor.ultrasavenothing.allowlist.domain.AddAllowedAppUseCase
import com.abaiaor.ultrasavenothing.allowlist.domain.FakeAllowlistRepository
import com.abaiaor.ultrasavenothing.allowlist.domain.FakeInstalledAppsRepository
import com.abaiaor.ultrasavenothing.allowlist.domain.GetInstalledAppsUseCase
import com.abaiaor.ultrasavenothing.allowlist.domain.ObserveAllowlistUseCase
import com.abaiaor.ultrasavenothing.allowlist.domain.PinnedApps
import com.abaiaor.ultrasavenothing.allowlist.domain.RemoveAllowedAppUseCase
import com.abaiaor.ultrasavenothing.coresystem.apps.InstalledApp
import com.abaiaor.ultrasavenothing.uicomponents.picker.AppPickerRowState
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AllowlistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var allowlistRepository: FakeAllowlistRepository
    private lateinit var viewModel: AllowlistViewModel

    private val installedApps = listOf(
        InstalledApp("com.example.maps", "Maps"),
        InstalledApp("com.example.music", "Music"),
    )

    private fun createViewModel(initiallyAllowed: Set<String> = emptySet()) {
        allowlistRepository = FakeAllowlistRepository(initiallyAllowed)
        viewModel = AllowlistViewModel(
            getInstalledAppsUseCase = GetInstalledAppsUseCase(FakeInstalledAppsRepository(installedApps)),
            observeAllowlistUseCase = ObserveAllowlistUseCase(allowlistRepository),
            addAllowedAppUseCase = AddAllowedAppUseCase(allowlistRepository),
            removeAllowedAppUseCase = RemoveAllowedAppUseCase(allowlistRepository),
        )
    }

    @Before
    fun setUp() {
        createViewModel()
    }

    @Test
    fun `WHEN loaded THEN Phone and Messages are always locked`() = runTest {
        val phone = viewModel.apps.value.first { it.packageName == PinnedApps.PHONE }
        val sms = viewModel.apps.value.first { it.packageName == PinnedApps.SMS }

        assertEquals(AppPickerRowState.Locked, phone.state)
        assertEquals(AppPickerRowState.Locked, sms.state)
    }

    @Test
    fun `WHEN loaded THEN installed apps not yet allowed are addable`() = runTest {
        val maps = viewModel.apps.value.first { it.packageName == "com.example.maps" }

        assertEquals(AppPickerRowState.Addable, maps.state)
    }

    @Test
    fun `WHEN loaded with app already allowed THEN that app shows as added`() = runTest {
        createViewModel(initiallyAllowed = setOf("com.example.maps"))

        val maps = viewModel.apps.value.first { it.packageName == "com.example.maps" }

        assertEquals(AppPickerRowState.Added, maps.state)
    }

    @Test
    fun `WHEN adding an app THEN it becomes added immediately`() = runTest {
        viewModel.onAddApp("com.example.maps")

        val maps = viewModel.apps.value.first { it.packageName == "com.example.maps" }
        assertEquals(AppPickerRowState.Added, maps.state)
    }

    @Test
    fun `WHEN removing an app THEN it becomes addable immediately`() = runTest {
        createViewModel(initiallyAllowed = setOf("com.example.maps"))

        viewModel.onRemoveApp("com.example.maps")

        val maps = viewModel.apps.value.first { it.packageName == "com.example.maps" }
        assertEquals(AppPickerRowState.Addable, maps.state)
    }

    @Test
    fun `WHEN cap is reached THEN a cap reached event is emitted`() = runTest(mainDispatcherRule.testDispatcher) {
        val fullAllowlist = (1..AddAllowedAppUseCase.MAX_ALLOWED_APPS).map { "com.example.app$it" }.toSet()
        createViewModel(initiallyAllowed = fullAllowlist)

        val receivedEvents = mutableListOf<Unit>()
        val collectJob = launch {
            viewModel.capReachedEvents.toList(receivedEvents)
        }

        viewModel.onAddApp("com.example.maps")

        assertEquals(1, receivedEvents.size)
        collectJob.cancel()
    }
}
