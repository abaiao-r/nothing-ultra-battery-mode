package com.abaiaor.ultrasavenothing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abaiaor.ultrasavenothing.ui.PlaceholderScreen
import com.abaiaor.ultrasavenothing.uicomponents.theme.UltraSaveNothingTheme

/**
 * Root composable. Wraps the app in the shared black theme from :ui-components and hosts
 * the (future) nav graph. Currently shows a placeholder screen until the first feature
 * screens are wired in.
 */
@Composable
fun UltraSaveNothingRoot() {
    UltraSaveNothingTheme {
        PlaceholderScreen(modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
private fun UltraSaveNothingRootPreview() {
    UltraSaveNothingRoot()
}
