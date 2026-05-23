package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.LocalEmojiFontFamily
import dev.tohure.muchik_dictionary.core.design.Ocean
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordEntry
import dictionarymuchik.shared.generated.resources.Res
import dictionarymuchik.shared.generated.resources.dict_header_category
import dictionarymuchik.shared.generated.resources.dict_header_icon
import dictionarymuchik.shared.generated.resources.dict_header_muchik
import dictionarymuchik.shared.generated.resources.dict_header_notes
import dictionarymuchik.shared.generated.resources.dict_header_spanish
import org.jetbrains.compose.resources.stringResource

@Composable
fun WordListHeader(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                    shape = MaterialTheme.shapes.medium
                ).padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.dict_header_icon),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(48.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.dict_header_muchik),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f),
        )
        Text(
            text = stringResource(Res.string.dict_header_spanish),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f),
        )
        Text(
            text = stringResource(Res.string.dict_header_category),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.2f),
        )
        Text(
            text = stringResource(Res.string.dict_header_notes),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f),
        )
    }
}

@Composable
fun WordListItem(entry: WordEntry, modifier: Modifier = Modifier) {
    val emojiFont = LocalEmojiFontFamily.current

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
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
            modifier =
                Modifier
                    .weight(1.2f)
                    .padding(end = 8.dp),
        ) {
            CategoryChip(category = entry.category)
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
