package com.abaiaor.ultrasavenothing.uicomponents.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val UltraSaveNothingColorScheme = darkColorScheme(
    primary = UltraAccent,
    onPrimary = UltraBlack,
    background = UltraBlack,
    onBackground = UltraOnBlack,
    surface = UltraSurface,
    onSurface = UltraOnBlack,
    secondary = UltraMuted,
)

/**
 * Shared black minimalist theme for the whole app. Feature screens must consume these
 * tokens rather than defining their own colors/typography/shapes (see
 * .github/instructions/compose.instructions.md).
 */
@Composable
fun UltraSaveNothingTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = UltraSaveNothingColorScheme,
        typography = UltraSaveNothingTypography,
        shapes = UltraSaveNothingShapes,
        content = content,
    )
}
