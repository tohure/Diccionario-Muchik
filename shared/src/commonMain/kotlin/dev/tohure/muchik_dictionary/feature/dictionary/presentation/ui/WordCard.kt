package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tohure.muchik_dictionary.core.design.LocalEmojiFontFamily
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordEntry
import dictionarymuchik.shared.generated.resources.Res
import dictionarymuchik.shared.generated.resources.dict_source_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun WordCard(entry: WordEntry, modifier: Modifier = Modifier) {
    val emojiFont = LocalEmojiFontFamily.current
    val hasSource = entry.sourceReference.isNotBlank() && !entry.sourceReference.startsWith("TODO")

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
            ) {
                CategoryChip(category = entry.category)

                Text(
                    text = entry.muchikTerm,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp),
                )
                Text(
                    text = entry.spanishTranslation,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp),
                )
                if (entry.etymologyNote.isNotBlank()) {
                    Text(
                        text = entry.etymologyNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                if (hasSource) {
                    Row(modifier = Modifier.padding(top = 6.dp)) {
                        Text(
                            text = stringResource(Res.string.dict_source_label) + " ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = entry.sourceReference,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = FontStyle.Italic,
                        )
                    }
                }
            }

            if (entry.emoji.isNotBlank()) {
                Text(
                    text = entry.emoji,
                    fontSize = 52.sp,
                    fontFamily = emojiFont,
                    modifier =
                        Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 12.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                )
            }
        }
    }
}
