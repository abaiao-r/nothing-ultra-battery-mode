package com.abaiaor.ultrasavenothing.ultramode.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abaiaor.ultrasavenothing.ultramode.domain.GetShellInstalledAppsUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.LaunchShellAppUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.ObserveShellAllowlistUseCase
import com.abaiaor.ultrasavenothing.ultramode.domain.ShellPinnedEntries
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Drives [UltraShellScreen]: the pinned Phone/Messages entries are always shown first, followed
 * by the currently allowed apps (in installed-apps label order), and taps launch the tapped app.
 */
@HiltViewModel
class UltraShellViewModel @Inject constructor(
    observeShellAllowlistUseCase: ObserveShellAllowlistUseCase,
    private val getShellInstalledAppsUseCase: GetShellInstalledAppsUseCase,
    private val launchShellAppUseCase: LaunchShellAppUseCase,
) : ViewModel() {

    val entries: StateFlow<List<ShellEntryUiState>> = observeShellAllowlistUseCase()
        .map { allowedPackageNames -> buildEntries(allowedPackageNames) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = buildEntries(emptySet()),
        )

    private fun buildEntries(allowedPackageNames: Set<String>): List<ShellEntryUiState> {
        val installedApps = getShellInstalledAppsUseCase()
        val installedLabelsByPackage = installedApps.associate { it.packageName to it.label }

        val pinnedEntries = listOf(
            ShellEntryUiState(packageName = ShellPinnedEntries.PHONE, label = "Phone", isPinned = true),
            ShellEntryUiState(packageName = ShellPinnedEntries.SMS, label = "Messages", isPinned = true),
        )

        val allowedEntries = allowedPackageNames
            .map { packageName ->
                ShellEntryUiState(
                    packageName = packageName,
                    label = installedLabelsByPackage[packageName] ?: packageName,
                    isPinned = false,
                )
            }
            .sortedBy { it.label.lowercase() }

        return pinnedEntries + allowedEntries
    }

    fun onAppClick(packageName: String) {
        launchShellAppUseCase(packageName)
    }
}
