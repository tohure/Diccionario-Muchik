package dev.tohure.muchik_dictionary.feature.numbers.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Ocean
import dev.tohure.muchik_dictionary.core.design.OceanContainer
import dev.tohure.muchik_dictionary.core.design.Sand
import dev.tohure.muchik_dictionary.feature.numbers.domain.model.NumberEntry
import dev.tohure.muchik_dictionary.feature.numbers.domain.model.NumeralClassifier
import dev.tohure.muchik_dictionary.feature.numbers.presentation.state.NumbersUiState
import dev.tohure.muchik_dictionary.feature.numbers.presentation.viewmodel.NumbersViewModel
import dictionarymuchik.shared.generated.resources.Res
import dictionarymuchik.shared.generated.resources.numbers_age_example
import dictionarymuchik.shared.generated.resources.numbers_age_note
import dictionarymuchik.shared.generated.resources.numbers_age_question_muchik
import dictionarymuchik.shared.generated.resources.numbers_age_question_spanish
import dictionarymuchik.shared.generated.resources.numbers_age_title
import dictionarymuchik.shared.generated.resources.numbers_classifiers_title
import dictionarymuchik.shared.generated.resources.numbers_formula_label
import dictionarymuchik.shared.generated.resources.numbers_practical_result
import dictionarymuchik.shared.generated.resources.numbers_practical_title
import dictionarymuchik.shared.generated.resources.numbers_prefixes_title
import dictionarymuchik.shared.generated.resources.numbers_reference_title
import dictionarymuchik.shared.generated.resources.numbers_subtitle
import dictionarymuchik.shared.generated.resources.numbers_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NumbersScreen(modifier: Modifier = Modifier) {
    val viewModel: NumbersViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NumbersContent(
        uiState = uiState,
        classifiers = NumeralClassifier.all,
        allNumbers = NumberEntry.all,
        onIncrement = viewModel::onIncrement,
        onDecrement = viewModel::onDecrement,
        modifier = modifier,
    )
}

@Composable
private fun NumbersContent(
    uiState: NumbersUiState,
    classifiers: List<NumeralClassifier>,
    allNumbers: List<NumberEntry>,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth > 600.dp

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            // Header
            item { NumbersHeader() }

            // Formula bar
            item { FormulaBar() }

            // Prefixes + age example
            if (isWide) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        PrefixesCard(modifier = Modifier.weight(1f))
                        AgeApplicationCard(modifier = Modifier.weight(1f))
                    }
                }
            } else {
                item { PrefixesCard(modifier = Modifier.fillMaxWidth()) }
                item { AgeApplicationCard(modifier = Modifier.fillMaxWidth()) }
            }

            // Practical example 12 hombres
            item { PracticalExampleCard() }

            // Classifier table
            item {
                SectionTitle(stringResource(Res.string.numbers_classifiers_title))
                Spacer(Modifier.height(8.dp))
                NumeralClassifierTable(classifiers = classifiers, modifier = Modifier.fillMaxWidth())
            }

            // Counter + reference list
            if (isWide) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ) {
                        CounterSection(
                            uiState = uiState,
                            onIncrement = onIncrement,
                            onDecrement = onDecrement,
                            modifier = Modifier.weight(1f),
                        )
                        NumberReferenceList(
                            numbers = allNumbers,
                            classifiers = classifiers,
                            isWide = true,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            } else {
                item {
                    CounterSection(
                        uiState = uiState,
                        onIncrement = onIncrement,
                        onDecrement = onDecrement,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    NumberReferenceList(
                        numbers = allNumbers,
                        classifiers = classifiers,
                        isWide = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun NumbersHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(Res.string.numbers_title),
            style = MaterialTheme.typography.headlineLarge,
            fontFamily = FontFamily.Serif,
            color = DarkClay,
        )
        Text(
            text = stringResource(Res.string.numbers_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontStyle = FontStyle.Italic,
        )
    }
}

@Composable
private fun FormulaBar() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E2E), MaterialTheme.shapes.medium)
                .padding(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.numbers_formula_label),
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF6C6C8A),
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.horizontalScroll(rememberScrollState()),
        ) {
            FormulaToken("[Prefijo Base]", Color(0xFF89DDFF))
            FormulaToken("+", Color(0xFF6C6C8A))
            FormulaToken("[Clasificador]", Color(0xFFFFCB6B))
            FormulaToken("+", Color(0xFF6C6C8A))
            FormulaToken("allo", Color(0xFFFFFFFF))
            FormulaToken("+", Color(0xFF6C6C8A))
            FormulaToken("[Unidad]", Color(0xFF89DDFF))
            FormulaToken("+", Color(0xFF6C6C8A))
            FormulaToken("[Objeto]", Color(0xFFC3E88D))
        }
    }
}

@Composable
private fun FormulaToken(text: String, color: Color) {
    Text(
        text = text,
        fontFamily = FontFamily.Monospace,
        fontSize = 13.sp,
        color = color,
        fontWeight = if (text == "allo") FontWeight.Bold else FontWeight.Normal,
    )
}

@Composable
private fun PrefixesCard(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .background(Color(0xFFFFFBEB), MaterialTheme.shapes.medium)
                .border(1.dp, Color(0xFFFDE68A), MaterialTheme.shapes.medium)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.numbers_prefixes_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DarkClay,
        )
        val prefixes =
            listOf(
                "Na-" to "10",
                "Tzhaxlltzha-" to "60",
                "Pac-" to "20",
                "Ñite-" to "70",
                "Çoc-" to "30",
                "Langæss-" to "80",
                "Noc-" to "40",
                "Tap-" to "90",
                "Exllmætzh-" to "50",
                "" to "",
            )
        prefixes.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                pair.forEach { (prefix, value) ->
                    if (prefix.isNotEmpty()) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = prefix,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = Ocean,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = "($value)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun AgeApplicationCard(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .background(OceanContainer.copy(alpha = 0.3f), MaterialTheme.shapes.medium)
                .border(1.dp, Ocean.copy(alpha = 0.3f), MaterialTheme.shapes.medium)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.numbers_age_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Ocean,
        )
        Text(
            text = stringResource(Res.string.numbers_age_question_muchik),
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = DarkClay,
        )
        Text(
            text = stringResource(Res.string.numbers_age_question_spanish),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontStyle = FontStyle.Italic,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.numbers_age_example),
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Serif,
            color = DarkClay,
        )
        Text(
            text = stringResource(Res.string.numbers_age_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontStyle = FontStyle.Italic,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PracticalExampleCard() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFBEB), MaterialTheme.shapes.medium)
                .border(1.dp, Color(0xFFFDE68A), MaterialTheme.shapes.medium)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(Res.string.numbers_practical_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DarkClay,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ExampleBlock("Na", "Prefijo\n(10)", Color(0xFFFDE68A))
            Text(
                "+",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            ExampleBlock("pong", "Clasificador\n(Personas)", Color(0xFFFDE68A))
            Text(
                "+",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            ExampleBlock("allo", "Conector", null)
            Text(
                "+",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            ExampleBlock("atput", "Unidad\n(2)", OceanContainer)
            Text(
                "+",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            ExampleBlock("ñofæn", "Sustantivo\n(Hombre)", null)
        }
        Text(
            text = stringResource(Res.string.numbers_practical_result),
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = DarkClay,
        )
    }
}

@Composable
private fun ExampleBlock(term: String, label: String, highlight: Color?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .background(
                    highlight ?: MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.small,
                ).border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.small)
                .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        Text(
            text = term,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = DarkClay,
            textAlign = TextAlign.Center,
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp,
        )
    }
}

@Composable
private fun CounterSection(
    uiState: NumbersUiState,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionTitle("Unidades Básicas (Práctica)")
        NumberCounter(
            entry = uiState.currentEntry,
            canDecrement = uiState.canDecrement,
            canIncrement = uiState.canIncrement,
            onDecrement = onDecrement,
            onIncrement = onIncrement,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun NumberReferenceList(
    numbers: List<NumberEntry>,
    classifiers: List<NumeralClassifier>,
    isWide: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large),
    ) {
        // Header fijo (no scrollea)
        Text(
            text = stringResource(Res.string.numbers_reference_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DarkClay,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
        HorizontalDivider()

        // Lista completa con scroll interno
        Column(
            modifier =
                Modifier
                    .heightIn(max = 450.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            numbers.forEachIndexed { index, entry ->
                ReferenceRow(
                    badge = entry.value?.toString() ?: "∞",
                    term = entry.muchikTerm,
                    label = entry.spanishTranslation,
                    isClassifier = false,
                    isWide = isWide,
                )
                if (index < numbers.lastIndex || classifiers.isNotEmpty()) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }
            classifiers.forEachIndexed { index, classifier ->
                ReferenceRow(
                    badge = "🏷️",
                    term = classifier.muchikTerm,
                    label = classifier.shortLabel,
                    isClassifier = true,
                    isWide = isWide,
                )
                if (index < classifiers.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }
        }
    }
}

@Composable
private fun ReferenceRow(
    badge: String,
    term: String,
    label: String,
    isClassifier: Boolean,
    isWide: Boolean,
) {
    // En clasificadores: wide = label a la derecha, narrow = label debajo
    // En números: siempre horizontal (label corto)
    val classifierHorizontal = isClassifier && isWide

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = if (classifierHorizontal || !isClassifier) Alignment.CenterVertically else Alignment.Top,
    ) {
        Box(
            modifier =
                Modifier
                    .background(Sand.copy(alpha = 0.8f), MaterialTheme.shapes.small)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = badge,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Clay,
            )
        }

        if (isClassifier && !isWide) {
            // Narrow + clasificador: término encima, label debajo
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = term,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                )
            }
        } else {
            // Wide o número: término a la izquierda, label a la derecha
            Text(
                text = term,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic,
                modifier =
                    Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            MaterialTheme.shapes.small,
                        ).padding(horizontal = 6.dp, vertical = 2.dp),
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = DarkClay,
    )
}
