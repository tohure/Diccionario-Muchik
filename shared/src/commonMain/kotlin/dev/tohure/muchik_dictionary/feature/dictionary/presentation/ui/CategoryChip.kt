package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.categoryColor

@Composable
fun CategoryChip(category: String, modifier: Modifier = Modifier) {
    val color = categoryColor(category)
    Box(
        modifier =
            modifier
                .background(color.copy(alpha = 0.12f), MaterialTheme.shapes.small)
                .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}
