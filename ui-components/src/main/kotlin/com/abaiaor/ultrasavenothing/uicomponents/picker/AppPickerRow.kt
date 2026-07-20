package com.abaiaor.ultrasavenothing.uicomponents.picker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abaiaor.ultrasavenothing.uicomponents.theme.UltraSaveNothingTheme

/**
 * Stateless row for browsing installed apps and adding/removing them from the Ultra Mode
 * allowlist. State is fully hoisted via [state]; the caller decides what [state] to pass
 * (addable/added/locked) based on its own allowlist data.
 *
 * @param label app name displayed next to the icon.
 * @param icon vector icon representing the app.
 * @param state current addable/added/locked visual and interaction state.
 * @param onAdd invoked when tapping the add control while [state] is [AppPickerRowState.Addable].
 * @param onRemove invoked when tapping the remove control while [state] is
 *   [AppPickerRowState.Added]. Never invoked while [state] is [AppPickerRowState.Locked].
 * @param modifier applied to the root element.
 */
@Composable
fun AppPickerRow(
    label: String,
    icon: ImageVector,
    state: AppPickerRowState,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(28.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f, fill = true),
        )
        when (state) {
            AppPickerRowState.Addable -> IconButton(
                onClick = onAdd,
                modifier = Modifier.semantics {
                    contentDescription = "Add $label"
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            AppPickerRowState.Added -> IconButton(
                onClick = onRemove,
                modifier = Modifier.semantics {
                    contentDescription = "Remove $label"
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            AppPickerRowState.Locked -> Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(24.dp)
                    .semantics { contentDescription = "$label is locked" },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPickerRowAddablePreview() {
    UltraSaveNothingTheme {
        AppPickerRow(
            label = "Maps",
            icon = Icons.Filled.Phone,
            state = AppPickerRowState.Addable,
            onAdd = {},
            onRemove = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPickerRowAddedPreview() {
    UltraSaveNothingTheme {
        AppPickerRow(
            label = "Maps",
            icon = Icons.Filled.Phone,
            state = AppPickerRowState.Added,
            onAdd = {},
            onRemove = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPickerRowLockedPreview() {
    UltraSaveNothingTheme {
        AppPickerRow(
            label = "Phone",
            icon = Icons.Filled.Phone,
            state = AppPickerRowState.Locked,
            onAdd = {},
            onRemove = {},
        )
    }
}
