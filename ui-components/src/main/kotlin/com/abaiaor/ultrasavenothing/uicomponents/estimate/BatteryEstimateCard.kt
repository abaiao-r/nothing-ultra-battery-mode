package com.abaiaor.ultrasavenothing.uicomponents.estimate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abaiaor.ultrasavenothing.uicomponents.theme.UltraSaveNothingTheme

/**
 * Stateless card displaying the current estimated remaining battery time. Pure display of
 * provided state — no business logic (formula lives in the estimation UseCase).
 *
 * @param estimateLabel human-readable remaining-time text (e.g. "18h 30m remaining"),
 *   already formatted by the caller.
 * @param modifier applied to the root element.
 */
@Composable
fun BatteryEstimateCard(
    estimateLabel: String,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Text(
                text = "Estimated battery time",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = estimateLabel,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BatteryEstimateCardHighPreview() {
    UltraSaveNothingTheme {
        BatteryEstimateCard(estimateLabel = "18h 30m remaining")
    }
}

@Preview(showBackground = true)
@Composable
private fun BatteryEstimateCardLowPreview() {
    UltraSaveNothingTheme {
        BatteryEstimateCard(estimateLabel = "45m remaining")
    }
}

@Preview(showBackground = true)
@Composable
private fun BatteryEstimateCardZeroPreview() {
    UltraSaveNothingTheme {
        BatteryEstimateCard(estimateLabel = "0m remaining")
    }
}
