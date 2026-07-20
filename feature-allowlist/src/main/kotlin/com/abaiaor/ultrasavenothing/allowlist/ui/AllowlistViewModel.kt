package com.abaiaor.ultrasavenothing.allowlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abaiaor.ultrasavenothing.allowlist.domain.AddAllowedAppResult
import com.abaiaor.ultrasavenothing.allowlist.domain.AddAllowedAppUseCase
import com.abaiaor.ultrasavenothing.allowlist.domain.GetInstalledAppsUseCase
import com.abaiaor.ultrasavenothing.allowlist.domain.ObserveAllowlistUseCase
import com.abaiaor.ultrasavenothing.allowlist.domain.PinnedApps
import com.abaiaor.ultrasavenothing.allowlist.domain.RemoveAllowedAppUseCase
import com.abaiaor.ultrasavenothing.uicomponents.picker.AppPickerRowState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Exposes the installed-apps list combined with the live allowlist state to [AllowlistScreen],
 * and routes add/remove intents through the existing UseCases — never touching a Repository
 * directly.
 */
@HiltViewModel
class AllowlistViewModel @Inject constructor(
    getInstalledAppsUseCase: GetInstalledAppsUseCase,
    observeAllowlistUseCase: ObserveAllowlistUseCase,
    private val addAllowedAppUseCase: AddAllowedAppUseCase,
    private val removeAllowedAppUseCase: RemoveAllowedAppUseCase,
) : ViewModel() {

    private val installedApps = getInstalledAppsUseCase()

    private val _capReachedEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    /** Emits whenever an add attempt is rejected because the 10-app cap was already reached. */
    val capReachedEvents: SharedFlow<Unit> = _capReachedEvents

    val apps: StateFlow<List<AllowlistAppUiState>> = observeAllowlistUseCase()
        .map { allowedPackageNames -> buildRows(allowedPackageNames) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = buildRows(emptySet()),
        )

    private fun buildRows(allowedPackageNames: Set<String>): List<AllowlistAppUiState> {
        val pinnedRows = listOf(
            AllowlistAppUiState(PinnedApps.PHONE, "Phone", AppPickerRowState.Locked),
            AllowlistAppUiState(PinnedApps.SMS, "Messages", AppPickerRowState.Locked),
        )
        val installedRows = installedApps.map { app ->
            val state = if (app.packageName in allowedPackageNames) {
                AppPickerRowState.Added
            } else {
                AppPickerRowState.Addable
            }
            AllowlistAppUiState(app.packageName, app.label, state)
        }
        return pinnedRows + installedRows
    }

    /** Called when the user taps the add control for [packageName]. */
    fun onAddApp(packageName: String) {
        viewModelScope.launch {
            val result = addAllowedAppUseCase(packageName)
            if (result is AddAllowedAppResult.CapReached) {
                _capReachedEvents.tryEmit(Unit)
            }
        }
    }

    /** Called when the user taps the remove control for [packageName]. */
    fun onRemoveApp(packageName: String) {
        viewModelScope.launch {
            removeAllowedAppUseCase(packageName)
        }
    }
}
