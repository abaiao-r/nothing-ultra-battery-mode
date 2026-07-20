package com.abaiaor.ultrasavenothing.ultramode.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abaiaor.ultrasavenothing.uicomponents.toggle.UltraModeToggle

/**
 * Single screen showing whether Ultra Mode is on or off and letting the user toggle it.
 * All state and toggle handling is delegated to [UltraModeViewModel].
 */
@Composable
fun UltraModeScreen(
    modifier: Modifier = Modifier,
    viewModel: UltraModeViewModel = hiltViewModel(),
) {
    val isEnabled by viewModel.isEnabled.collectAsStateWithLifecycle()

    UltraModeScreenContent(
        isEnabled = isEnabled,
        onToggle = viewModel::onToggle,
        modifier = modifier,
    )
}

@Composable
internal fun UltraModeScreenContent(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = if (isEnabled) "Ultra Mode is on" else "Ultra Mode is off",
            style = MaterialTheme.typography.titleLarge,
        )
        UltraModeToggle(
            isEnabled = isEnabled,
            onToggle = onToggle,
            modifier = Modifier.padding(top = 24.dp),
        )
    }
}
