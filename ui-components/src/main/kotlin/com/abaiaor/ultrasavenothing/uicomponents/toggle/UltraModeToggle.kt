package com.abaiaor.ultrasavenothing.uicomponents.toggle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abaiaor.ultrasavenothing.uicomponents.theme.UltraSaveNothingTheme

/**
 * Stateless switch that turns Ultra Mode on/off. State is fully hoisted: the caller owns
 * [isEnabled] and reacts to [onToggle]. Styled with the shared black minimalist theme tokens.
 *
 * @param isEnabled current Ultra Mode state to display.
 * @param onToggle invoked with the new desired state when the user taps the switch.
 * @param modifier applied to the root element.
 */
@Composable
fun UltraModeToggle(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            modifier = Modifier.semantics {
                contentDescription = ULTRA_MODE_TOGGLE_CONTENT_DESCRIPTION
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.surface,
            ),
        )
    }
}

/** Content description used for finding [UltraModeToggle] in component tests. */
const val ULTRA_MODE_TOGGLE_CONTENT_DESCRIPTION = "Ultra Mode toggle"

@Preview(showBackground = true)
@Composable
private fun UltraModeToggleOffPreview() {
    UltraSaveNothingTheme {
        UltraModeToggle(isEnabled = false, onToggle = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun UltraModeToggleOnPreview() {
    UltraSaveNothingTheme {
        UltraModeToggle(isEnabled = true, onToggle = {})
    }
}
