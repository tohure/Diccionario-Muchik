package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Sand
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordCategory

@Composable
fun CategoryFilterChip(
    category: WordCategory,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text = category.displayName, style = MaterialTheme.typography.labelMedium) },
        modifier = modifier.padding(end = 6.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Clay,
            selectedLabelColor = Sand,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = DarkClay,
        ),
    )
}
