package com.abaiaor.ultrasavenothing.uicomponents.estimate

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.abaiaor.ultrasavenothing.uicomponents.theme.UltraSaveNothingTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BatteryEstimateCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN estimate label is provided THEN card displays it verbatim`() {
        val estimateLabel = "18h 30m remaining"

        composeTestRule.setContent {
            UltraSaveNothingTheme {
                BatteryEstimateCard(estimateLabel = estimateLabel)
            }
        }

        composeTestRule.onNodeWithText(estimateLabel).assertTextEquals(estimateLabel)
    }

    @Test
    fun `WHEN estimate label changes THEN card displays the new value`() {
        val lowEstimateLabel = "45m remaining"

        composeTestRule.setContent {
            UltraSaveNothingTheme {
                BatteryEstimateCard(estimateLabel = lowEstimateLabel)
            }
        }

        composeTestRule.onNodeWithText(lowEstimateLabel).assertTextEquals(lowEstimateLabel)
    }

    @Test
    fun `WHEN card is shown THEN static label is displayed`() {
        composeTestRule.setContent {
            UltraSaveNothingTheme {
                BatteryEstimateCard(estimateLabel = "1h remaining")
            }
        }

        composeTestRule.onNodeWithText("Estimated battery time").assertTextEquals(
            "Estimated battery time",
        )
    }
}
