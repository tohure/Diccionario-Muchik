package dev.tohure.muchik_dictionary.feature.grammar.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.Sand
import dictionarymuchik.shared.generated.resources.Res
import dictionarymuchik.shared.generated.resources.grammar_verb_table_example
import dictionarymuchik.shared.generated.resources.grammar_verb_table_mark
import dictionarymuchik.shared.generated.resources.grammar_verb_table_pronoun
import org.jetbrains.compose.resources.stringResource

private data class ConjugationRow(
    val pronoun: String,
    val mark: String,
    val prefix: String,
    val suffix: String,
)

private val ROWS =
    listOf(
        ConjugationRow("Yo", "-eñ / -ñ", "Peñ", "eñ chi"),
        ConjugationRow("Tú", "-as / -s", "Peñ", "as chi"),
        ConjugationRow("Él/Ella", "-ang / -ng", "Peñ", "ang chi"),
        ConjugationRow("Nosotros", "-esh / -sh", "Peñ", "esh chi"),
        ConjugationRow("Ustedes", "-aschi", "Peñ", "aschi chi"),
        ConjugationRow("Ellos", "-ænang", "Peñ", "ænang chi"),
    )

@Composable
fun VerbConjugationTable(modifier: Modifier = Modifier) {
    val headerBg = MaterialTheme.colorScheme.surfaceVariant
    val oddBg = MaterialTheme.colorScheme.surface
    val evenBg = Sand.copy(alpha = 0.15f)

    androidx.compose.foundation.layout.Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(headerBg)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.grammar_verb_table_pronoun),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(Res.string.grammar_verb_table_mark),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(Res.string.grammar_verb_table_example),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1.5f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        ROWS.forEachIndexed { index, row ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(if (index % 2 == 0) oddBg else evenBg)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = row.pronoun,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = row.mark,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Clay,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text =
                        buildAnnotatedString {
                            append(row.prefix)
                            withStyle(SpanStyle(color = Clay, fontWeight = FontWeight.Bold)) {
                                append(row.suffix.substringBefore(" "))
                            }
                            append(" " + row.suffix.substringAfter(" "))
                        },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1.5f),
                )
            }
        }
    }
}
