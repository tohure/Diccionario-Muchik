package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.categoryColor
import dev.tohure.muchik_dictionary.core.design.categoryColorMap

@Composable
fun DonutChart(
    categoryCounts: Map<String, Int>,
    modifier: Modifier = Modifier,
) {
    if (categoryCounts.isEmpty()) return

    val total = categoryCounts.values.sum().coerceAtLeast(1).toFloat()
    val entries = categoryCounts.entries.sortedByDescending { it.value }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val diameter = minOf(size.width, size.height)
            val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
            val arcSize = Size(diameter, diameter)
            val center = Offset(size.width / 2f, size.height / 2f)
            val holeRadius = diameter * 0.25f

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
                color = Color.White,
                radius = holeRadius,
                center = center,
            )
        }

        val half = (entries.size + 1) / 2
        val leftColumn = entries.take(half)
        val rightColumn = entries.drop(half)

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                leftColumn.forEach { (category, _) ->
                    LegendItem(category = category)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                rightColumn.forEach { (category, _) ->
                    LegendItem(category = category)
                }
            }
        }
    }
}

@Composable
private fun LegendItem(category: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(categoryColor(category), RoundedCornerShape(2.dp)),
        )
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp),
            maxLines = 1,
        )
    }
}
