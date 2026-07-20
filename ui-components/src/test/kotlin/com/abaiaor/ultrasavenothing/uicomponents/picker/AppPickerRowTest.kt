package com.abaiaor.ultrasavenothing.uicomponents.picker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.test.assertIsDisplayed
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
class AppPickerRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN state is addable AND add control is tapped THEN onAdd fires`() {
        var added = false

        composeTestRule.setContent {
            UltraSaveNothingTheme {
                AppPickerRow(
                    label = "Maps",
                    icon = Icons.Filled.Phone,
                    state = AppPickerRowState.Addable,
                    onAdd = { added = true },
                    onRemove = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Add Maps").performClick()

        assert(added) { "Expected onAdd to be invoked when tapping the add control" }
    }

    @Test
    fun `WHEN state is added AND remove control is tapped THEN onRemove fires`() {
        var removed = false

        composeTestRule.setContent {
            UltraSaveNothingTheme {
                AppPickerRow(
                    label = "Maps",
                    icon = Icons.Filled.Phone,
                    state = AppPickerRowState.Added,
                    onAdd = {},
                    onRemove = { removed = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Remove Maps").performClick()

        assert(removed) { "Expected onRemove to be invoked when tapping the remove control" }
    }

    @Test
    fun `WHEN state is locked THEN there is no remove control to trigger a callback`() {
        var removed = false

        composeTestRule.setContent {
            UltraSaveNothingTheme {
                AppPickerRow(
                    label = "Phone",
                    icon = Icons.Filled.Phone,
                    state = AppPickerRowState.Locked,
                    onAdd = {},
                    onRemove = { removed = true },
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Phone is locked")
            .assertIsDisplayed()

        assert(!removed) { "Locked state must never invoke onRemove" }
    }
}
