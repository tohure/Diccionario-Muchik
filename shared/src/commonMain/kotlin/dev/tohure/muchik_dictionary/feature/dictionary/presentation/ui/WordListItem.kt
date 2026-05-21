package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.LocalEmojiFontFamily
import dev.tohure.muchik_dictionary.core.design.Ocean
import dev.tohure.muchik_dictionary.core.design.Sand
import dev.tohure.muchik_dictionary.core.design.categoryColor
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordEntry

@Composable
fun WordListHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Sand.copy(alpha = 0.5f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "ÍCONO",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(48.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "MUCHIK",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1.5f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "ESPAÑOL",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(2f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "CATEGORÍA",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1.2f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "NOTAS",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(2f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}

@Composable
fun WordListItem(entry: WordEntry, modifier: Modifier = Modifier) {
    val catColor = categoryColor(entry.category)
    val emojiFont = LocalEmojiFontFamily.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.width(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (entry.emoji.isNotBlank()) {
                Text(
                    text = entry.emoji,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = emojiFont,
                )
            }
        }

        Text(
            text = entry.muchikTerm,
            style = MaterialTheme.typography.titleSmall,
            color = DarkClay,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f),
        )

        Text(
            text = entry.spanishTranslation,
            style = MaterialTheme.typography.bodySmall,
            color = Ocean,
            modifier = Modifier.weight(2f),
        )

        Box(
            modifier = Modifier
                .weight(1.2f)
                .padding(end = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .background(catColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    text = entry.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = catColor,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                )
            }
        }

        Text(
            text = entry.etymologyNote,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.weight(2f),
            maxLines = 2,
        )
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}
