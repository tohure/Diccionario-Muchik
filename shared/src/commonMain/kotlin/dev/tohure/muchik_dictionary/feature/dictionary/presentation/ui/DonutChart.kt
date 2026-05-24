package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.categoryColor
import dev.tohure.muchik_dictionary.core.design.categoryColorMap

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DonutChart(
    categoryCounts: Map<String, Int>,
    modifier: Modifier = Modifier,
) {
    if (categoryCounts.isEmpty()) return

    val total = categoryCounts.values
        .sum()
        .coerceAtLeast(1)
        .toFloat() // evita división por cero en los ángulos de arco
    val entries = categoryCounts.entries.sortedByDescending { it.value }
    val surfaceColor = MaterialTheme.colorScheme.surface

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val isWide = maxWidth > 450.dp

        if (isWide) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ChartCanvas(entries, total, surfaceColor, Modifier.size(140.dp))
                Spacer(modifier = Modifier.width(24.dp))
                FlowRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    entries.forEach { (category, _) ->
                        LegendItem(category = category)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ChartCanvas(entries, total, surfaceColor, Modifier.size(160.dp))
                Spacer(modifier = Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    entries.forEach { (category, _) ->
                        LegendItem(category = category, modifier = Modifier.padding(horizontal = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ChartCanvas(
    entries: List<Map.Entry<String, Int>>,
    total: Float,
    surfaceColor: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val diameter = minOf(size.width, size.height)
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val arcSize = Size(diameter, diameter)
        val center = Offset(size.width / 2f, size.height / 2f)
        val holeRadius = diameter * 0.28f

        var startAngle = -90f
        entries.forEach { (category, count) ->
            val sweepAngle = 360f * count / total
            val color = categoryColorMap[category] ?: Clay
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = topLeft,
                size = arcSize,
            )
            startAngle += sweepAngle
        }

        drawCircle(
            color = surfaceColor,
            radius = holeRadius,
            center = center,
        )
    }
}

@Composable
private fun LegendItem(category: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 2.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(12.dp)
                    .background(categoryColor(category), RoundedCornerShape(3.dp)),
        )
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 6.dp),
            maxLines = 1,
        )
    }
}
