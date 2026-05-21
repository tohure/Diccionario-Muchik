package dev.tohure.muchik_dictionary.feature.numbers.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Ocean
import dev.tohure.muchik_dictionary.core.design.Sand
import dev.tohure.muchik_dictionary.feature.numbers.domain.model.ClassifierGroup
import dev.tohure.muchik_dictionary.feature.numbers.domain.model.NumeralClassifier

@Composable
fun NumeralClassifierTable(
    classifiers: List<NumeralClassifier>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ClassifierGroup.entries.forEach { group ->
            val items = classifiers.filter { it.group == group }
            ClassifierGroupSection(group = group, items = items)
        }
    }
}

@Composable
private fun ClassifierGroupSection(
    group: ClassifierGroup,
    items: List<NumeralClassifier>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), MaterialTheme.shapes.medium)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = group.emoji, style = MaterialTheme.typography.titleMedium)
            Text(
                text = group.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = DarkClay,
            )
        }

        items.forEach { classifier ->
            ClassifierRow(classifier = classifier)
        }
    }
}

@Composable
private fun ClassifierRow(
    classifier: NumeralClassifier,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = classifier.muchikTerm,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Ocean,
            modifier = Modifier
                .background(Sand.copy(alpha = 0.6f), MaterialTheme.shapes.small)
                .padding(horizontal = 8.dp, vertical = 2.dp),
        )
        Text(
            text = classifier.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
    }
}
