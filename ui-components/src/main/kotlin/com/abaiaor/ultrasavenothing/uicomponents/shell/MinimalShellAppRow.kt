package com.abaiaor.ultrasavenothing.uicomponents.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abaiaor.ultrasavenothing.uicomponents.theme.UltraSaveNothingTheme

/**
 * Stateless row representing a single entry in the minimal Ultra Mode home screen (Phone,
 * Messages, or an allowed app). Supports a visually distinct "pinned" style for entries that
 * are always present (Phone/SMS) versus regular allowed apps.
 *
 * @param label app/entry name displayed next to the icon.
 * @param icon vector icon representing the app/entry.
 * @param isPinned whether this row uses the pinned visual style (Phone/SMS).
 * @param onClick invoked when the row is tapped, launching the corresponding app.
 * @param modifier applied to the root element.
 */
@Composable
fun MinimalShellAppRow(
    label: String,
    icon: ImageVector,
    isPinned: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isPinned) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.background
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isPinned) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onBackground
            },
            modifier = Modifier.size(28.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = if (isPinned) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyLarge
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MinimalShellAppRowPinnedPreview() {
    UltraSaveNothingTheme {
        MinimalShellAppRow(
            label = "Phone",
            icon = Icons.Filled.Call,
            isPinned = true,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MinimalShellAppRowRegularPreview() {
    UltraSaveNothingTheme {
        MinimalShellAppRow(
            label = "Maps",
            icon = Icons.Filled.Home,
            isPinned = false,
            onClick = {},
        )
    }
}
