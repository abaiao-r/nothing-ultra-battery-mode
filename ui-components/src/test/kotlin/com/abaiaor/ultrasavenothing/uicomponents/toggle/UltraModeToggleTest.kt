package com.abaiaor.ultrasavenothing.uicomponents.toggle

import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.abaiaor.ultrasavenothing.uicomponents.theme.UltraSaveNothingTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UltraModeToggleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN isEnabled is false THEN toggle renders in the off state`() {
        composeTestRule.setContent {
            UltraSaveNothingTheme {
                UltraModeToggle(isEnabled = false, onToggle = {})
            }
        }

        composeTestRule
            .onNodeWithContentDescription(ULTRA_MODE_TOGGLE_CONTENT_DESCRIPTION)
            .assertIsOff()
    }

    @Test
    fun `WHEN isEnabled is true THEN toggle renders in the on state`() {
        composeTestRule.setContent {
            UltraSaveNothingTheme {
                UltraModeToggle(isEnabled = true, onToggle = {})
            }
        }

        composeTestRule
            .onNodeWithContentDescription(ULTRA_MODE_TOGGLE_CONTENT_DESCRIPTION)
            .assertIsOn()
    }

    @Test
    fun `WHEN the toggle is clicked THEN onToggle fires with the inverted state`() {
        var callbackValue: Boolean? = null

        composeTestRule.setContent {
            UltraSaveNothingTheme {
                UltraModeToggle(
                    isEnabled = false,
                    onToggle = { callbackValue = it },
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(ULTRA_MODE_TOGGLE_CONTENT_DESCRIPTION)
            .performClick()

        assert(callbackValue == true) {
            "Expected onToggle to be invoked with true, but was $callbackValue"
        }
    }
}
