package com.abaiaor.ultrasavenothing.allowlist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abaiaor.ultrasavenothing.allowlist.domain.PinnedApps
import com.abaiaor.ultrasavenothing.uicomponents.picker.AppPickerRow

/**
 * Screen listing installed apps with their current allowed/not-allowed state, letting the user
 * manage the Ultra Mode allowlist. All state and add/remove handling is delegated to
 * [AllowlistViewModel].
 */
@Composable
fun AllowlistScreen(
    modifier: Modifier = Modifier,
    viewModel: AllowlistViewModel = hiltViewModel(),
) {
    val apps by viewModel.apps.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.capReachedEvents.collect {
            snackbarHostState.showSnackbar("You can only allow up to 10 apps")
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackbarHostState)
        LazyColumn {
            items(items = apps, key = { it.packageName }) { app ->
                AppPickerRow(
                    label = app.label,
                    icon = if (app.packageName == PinnedApps.PHONE) Icons.Filled.Phone else Icons.Filled.Home,
                    state = app.state,
                    onAdd = { viewModel.onAddApp(app.packageName) },
                    onRemove = { viewModel.onRemoveApp(app.packageName) },
                )
            }
        }
    }
}
