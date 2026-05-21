package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.Gold
import dev.tohure.muchik_dictionary.core.design.Ocean

@Composable
fun SemanticDistributionChart(
    categoryCounts: Map<String, Int>,
    modifier: Modifier = Modifier,
) {
    if (categoryCounts.isEmpty()) return

    val sorted = categoryCounts.entries.sortedByDescending { it.value }.take(10)
    val maxCount = sorted.maxOf { it.value }.coerceAtLeast(1)
    val barColors = listOf(Clay, Ocean, Gold, Clay.copy(alpha = 0.7f), Ocean.copy(alpha = 0.7f))

    Column(modifier = modifier.fillMaxWidth()) {
        sorted.forEachIndexed { index, (category, count) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(0.35f),
                    maxLines = 1,
                )
                Canvas(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(16.dp)
                ) {
                    val fraction = count.toFloat() / maxCount
                    val barWidth = size.width * fraction
                    drawRoundRect(
                        color = barColors[index % barColors.size],
                        topLeft = Offset(0f, 2f),
                        size = Size(barWidth.coerceAtLeast(8f), size.height - 4f),
                        cornerRadius = CornerRadius(4f),
                    )
                }
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .weight(0.15f)
                        .padding(start = 4.dp),
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}
