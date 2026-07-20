package com.abaiaor.ultrasavenothing.uicomponents.shell

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.abaiaor.ultrasavenothing.uicomponents.theme.UltraSaveNothingTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MinimalShellAppRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN row is tapped THEN onClick callback fires`() {
        var clicked = false

        composeTestRule.setContent {
            UltraSaveNothingTheme {
                MinimalShellAppRow(
                    label = "Phone",
                    icon = Icons.Filled.Call,
                    isPinned = true,
                    onClick = { clicked = true },
                )
            }
        }

        composeTestRule.onNodeWithText("Phone").performClick()

        assert(clicked) { "Expected onClick to be invoked after tapping the row" }
    }

    @Test
    fun `WHEN label is provided THEN row displays it`() {
        composeTestRule.setContent {
            UltraSaveNothingTheme {
                MinimalShellAppRow(
                    label = "Messages",
                    icon = Icons.Filled.Call,
                    isPinned = true,
                    onClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Messages").assertIsDisplayed()
    }

    @Test
    fun `WHEN row is not pinned THEN it still renders and remains clickable`() {
        var clicked = false

        composeTestRule.setContent {
            UltraSaveNothingTheme {
                MinimalShellAppRow(
                    label = "Maps",
                    icon = Icons.Filled.Call,
                    isPinned = false,
                    onClick = { clicked = true },
                )
            }
        }

        composeTestRule.onNodeWithText("Maps").performClick()

        assert(clicked) { "Expected onClick to be invoked for a non-pinned row" }
    }
}
