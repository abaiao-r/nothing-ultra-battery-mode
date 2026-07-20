package com.abaiaor.ultrasavenothing.ultramode.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abaiaor.ultrasavenothing.uicomponents.shell.MinimalShellAppRow
import com.abaiaor.ultrasavenothing.ultramode.domain.ShellPinnedEntries

/**
 * Minimal home-screen shell shown while Ultra Mode is active: the always-pinned Phone/Messages
 * entries followed by the currently allowed apps, each launching its app on tap.
 */
@Composable
fun UltraShellScreen(
    modifier: Modifier = Modifier,
    viewModel: UltraShellViewModel = hiltViewModel(),
) {
    val entries by viewModel.entries.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(entries, key = { it.packageName }) { entry ->
                MinimalShellAppRow(
                    label = entry.label,
                    icon = entry.iconFor(),
                    isPinned = entry.isPinned,
                    onClick = { viewModel.onAppClick(entry.packageName) },
                )
            }
        }
    }
}

private fun ShellEntryUiState.iconFor(): ImageVector = when (packageName) {
    ShellPinnedEntries.PHONE -> Icons.Filled.Call
    ShellPinnedEntries.SMS -> Icons.Filled.MailOutline
    else -> Icons.Filled.Home
}
