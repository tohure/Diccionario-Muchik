package dev.tohure.muchik_dictionary.feature.grammar.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Ocean
import dev.tohure.muchik_dictionary.core.design.Sand

private val OCEAN_DARK = Color(0xFF1E3A8A)

@Composable
fun GrammarScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { GrammarHeader() }
        item { Block1WordFormation() }
        item { Block2Greetings() }
        item { Block3Postpositions() }
        item { Block4VerbConjugation() }
        item { Block5Syntax() }
        item { Block6InalienablePossession() }
        item { Block7Loanwords() }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun GrammarHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = "Estructura y Gramática Muchik",
            style = MaterialTheme.typography.headlineSmall,
            color = DarkClay,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "El Muchik es un idioma aglutinante, con una morfología compleja y marcas verbales " +
                "de posición estricta (Basado en F. de la Carrera y R. Eloranta).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontStyle = FontStyle.Italic,
        )
    }
}

@Composable
private fun Block1WordFormation() {
    data class SuffixEntry(val suffix: String, val label: String, val examples: String)

    val suffixes = listOf(
        SuffixEntry("-uk", "Sufijo Locativo — lugar", "apuk (aula), siaduk (cama)"),
        SuffixEntry("-ik", "Sufijo Instrumental — objeto usado", "apik (útiles), akik (lentes)"),
        SuffixEntry("-(a)pæk", "Sufijo Agente — quien realiza la acción", "apapæk (estudiante)"),
        SuffixEntry("-ko + pæk", "Agente Causativo — quien hace que otro actúe", "apkopæk (profesor)"),
        SuffixEntry("-(i)sæk", "Sufijo de Resultado", "læmisæk (la muerte), apisæk (la lección)"),
    )

    GrammarBlockCard(title = "1. Formación de Palabras (Derivación)") {
        Text(
            text = "El idioma crea nuevos sustantivos añadiendo sufijos a una raíz verbal (ej. raíz ap- = aprender):",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(10.dp))
        suffixes.forEach { entry ->
            SuffixItem(suffix = entry.suffix, label = entry.label, examples = entry.examples)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SuffixItem(suffix: String, label: String, examples: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = suffix,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = DarkClay,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Sand.copy(alpha = 0.35f))
                .border(1.dp, DarkClay.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp),
        )
        Text(
            text = buildAnnotatedString {
                append(label)
                append(". ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Clay)) {
                    append(examples)
                }
            },
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun Block2Greetings() {
    data class Greeting(val muchik: String, val spanish: String, val highlight: Boolean = false)

    val greetings = listOf(
        Greeting("¡Peño ænæm!", "¡Buenos días!"),
        Greeting("¡Peño nerræm!", "¡Buenas tardes!"),
        Greeting("¡Peño ciamo!", "¡Buena vida / Salud!", highlight = true),
        Greeting("¡Peño Tæsæk!", "¡Bienvenidos!"),
        Greeting("Loktopan / Loktopanchi", "Adiós (singular / plural)"),
    )

    GrammarBlockCard(title = "2. Saludos: La Regla del \"Peño\"") {
        Text(
            text = buildAnnotatedString {
                append("Para buenos deseos se usa la raíz ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Peñ") }
                append(" (bueno) unida al sufijo conector ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("-o") }
                append(":")
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(10.dp))
        greetings.forEach { greeting ->
            GreetingRow(greeting.muchik, greeting.spanish, greeting.highlight)
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun GreetingRow(muchik: String, spanish: String, highlight: Boolean) {
    val bgColor = if (highlight) Color(0xFFD1FAE5) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    val borderColor = if (highlight) Color(0xFF6EE7B7) else Color.Transparent
    val textColor = if (highlight) Color(0xFF065F46) else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = muchik,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = spanish,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}

@Composable
private fun Block3Postpositions() {
    data class Postposition(val word: String, val meaning: String, val example: String)

    val items = listOf(
        Postposition("nik", "Dentro / En", "An nik — En la casa"),
        Postposition("cápæc", "Encima", "Mesa cápæc — Sobre la mesa"),
        Postposition("ssecen", "Debajo", "Hon ssecen — Debajo del árbol"),
        Postposition("lucæc", "Entre", "Fær lucæc — Entre la tierra"),
        Postposition("tot", "Con (animado)", "Ef tot — Junto con el padre"),
        Postposition("fæiñ", "Con (objeto)", "Tonic fæiñ — Con la piedra"),
    )

    GrammarBlockCard(title = "3. Ubicación y Posposiciones Espaciales") {
        Text(
            text = buildAnnotatedString {
                append("El Muchik usa ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("posposiciones") }
                append(" (van ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("después") }
                append(" del sustantivo) para indicar ubicación:")
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(10.dp))
        items.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = item.word,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = DarkClay,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Sand.copy(alpha = 0.35f))
                        .border(1.dp, DarkClay.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                )
                Text(
                    text = item.meaning,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(0.7f),
                )
                Text(
                    text = item.example,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = Clay,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun Block4VerbConjugation() {
    GrammarBlockCard(title = "4. Marcas Verbales (Conjugación)") {
        Text(
            text = buildAnnotatedString {
                append("La marca de persona se engancha ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("a la primera palabra") }
                append(" de la frase. Ejemplo con el verbo \"Estar bien\" (Peñ chi):")
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        VerbConjugationTable()
    }
}

@Composable
private fun Block5Syntax() {
    GrammarBlockCard(title = "5. Sintaxis Básica") {
        Text(
            text = buildAnnotatedString {
                append("En Muchik ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("el adjetivo siempre precede al sustantivo")
                }
                append(". Regla de oro para armar frases descriptivas:")
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        SyntaxExampleBox(
            label = "Ejemplo 1: Hombre bueno",
            parts = listOf("Peñ" to "Bueno", "Ñofæn" to "Hombre"),
            result = "Peñ ñofæn",
            bgColor = Color(0xFFFEF3C7),
            borderColor = Color(0xFFFDE68A),
            resultColor = Clay,
        )
        Spacer(Modifier.height(10.dp))
        SyntaxExampleBox(
            label = "Ejemplo 2: Casa grande",
            parts = listOf("Ajpe" to "Grande", "An" to "Casa"),
            result = "Ajpe an",
            bgColor = Color(0xFFCCFBF1),
            borderColor = Color(0xFF99F6E4),
            resultColor = Ocean,
        )
    }
}

@Composable
private fun SyntaxExampleBox(
    label: String,
    parts: List<Pair<String, String>>,
    result: String,
    bgColor: Color,
    borderColor: Color,
    resultColor: Color,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(12.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = resultColor,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = buildAnnotatedString {
                parts.forEachIndexed { index, (muchik, spanish) ->
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(muchik) }
                    append(" (")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) { append(spanish) }
                    append(")")
                    if (index < parts.size - 1) append("  +  ")
                }
            },
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "→  $result",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = resultColor,
        )
    }
}

@Composable
private fun Block6InalienablePossession() {
    GrammarBlockCard(
        title = "6. Posesión Inalienable",
        containerColor = Color(0xFFFFF1F2),
        titleColor = DarkClay,
    ) {
        Text(
            text = "Las partes del cuerpo y los miembros de la familia son \"inalienables\": " +
                "siempre deben ir acompañadas de un posesivo, porque una \"mano\" no existe " +
                "separada de un cuerpo, ni una \"madre\" sin un hijo.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        PossessionBox(
            label = "Uso Incorrecto",
            labelColor = Clay,
            content = "Decir simplemente Mæcya (Mano) o Eng (Madre) como conceptos aislados.",
            bgColor = Color.White,
            borderColor = Color(0xFFFFCDD2),
        )
        Spacer(Modifier.height(8.dp))
        PossessionBox(
            label = "Uso Correcto (Con pronombres)",
            labelColor = Color(0xFF065F46),
            content = "Mæiñ mæcya (Mi mano)  •  Tzhan eng (Tu madre)  •  Cio ef (Su padre)",
            bgColor = Color.White,
            borderColor = Color(0xFFBBF7D0),
        )
    }
}

@Composable
private fun PossessionBox(
    label: String,
    labelColor: Color,
    content: String,
    bgColor: Color,
    borderColor: Color,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(12.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = labelColor,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun Block7Loanwords() {
    GrammarBlockCard(
        title = "7. Préstamos y Evolución Fonética",
        containerColor = Color(0xFFEFF6FF),
        titleColor = OCEAN_DARK,
    ) {
        Text(
            text = "El mochica interactuó con otros idiomas y adaptó vocabulario foráneo a su sistema fonético:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LoanwordBox(
                title = "Contacto con el Quechua",
                content = buildAnnotatedString {
                    append("La palabra quechua ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("'wakcha'") }
                    append(" (pobre/huérfano) fue adoptada y moldeada hasta convertirse en el adjetivo ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Faccia") }
                    append(".")
                },
                modifier = Modifier.weight(1f),
            )
            LoanwordBox(
                title = "Contacto con el Español",
                content = buildAnnotatedString {
                    append("El modismo onomatopéyico ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("'michi'") }
                    append(" para llamar a los felinos fue asimilado al fonetismo local como ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Miss") }
                    append(" (Gato).")
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun LoanwordBox(
    title: String,
    content: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(6.dp))
            .padding(12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = OCEAN_DARK,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
