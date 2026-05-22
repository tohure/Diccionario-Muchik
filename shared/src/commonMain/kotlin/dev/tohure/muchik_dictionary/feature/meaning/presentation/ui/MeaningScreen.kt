package dev.tohure.muchik_dictionary.feature.meaning.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Gold
import dev.tohure.muchik_dictionary.core.design.Ocean
import dev.tohure.muchik_dictionary.core.design.Sand

@Composable
fun MeaningScreen() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWide = maxWidth > 600.dp
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { ScreenHeader() }
            item { IntroText() }
            item { NameOriginSection(isWide = isWide) }
            item { EtymologyCard(isWide = isWide) }
            item { IdentityQuote() }
        }
    }
}

@Composable
private fun ScreenHeader() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "El Significado de \"Muchik\"",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkClay,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun IntroText() {
    Text(
        text = "A menudo usamos las palabras \"Mochica\", \"Muchik\" o \"Yunga\" como sinónimos, " +
            "pero históricamente tienen orígenes, pesos y significados muy distintos.",
        style = MaterialTheme.typography.bodyLarge,
        color = Clay,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
    )
}

@Composable
private fun NameOriginSection(isWide: Boolean) {
    if (isWide) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YungaCard(modifier = Modifier.weight(1f))
            MochicaCard(modifier = Modifier.weight(1f))
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            YungaCard(modifier = Modifier.fillMaxWidth())
            MochicaCard(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun YungaCard(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        HorizontalDivider(thickness = 4.dp, color = Clay)
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "1. El nombre impuesto: Yunga",
                style = MaterialTheme.typography.titleMedium,
                color = DarkClay,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Text(
                text = buildAnnotatedString {
                    append("Cuando los Incas y luego los españoles llegaron a la costa norte del Perú, " +
                        "llamaron al idioma y a la gente ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("\"Yunga\"") }
                    append(". Sin embargo, esta no es una palabra de nuestro idioma. Es una palabra en ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Quechua") }
                    append(" (")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("Yunka") }
                    append(") que significa \"valle cálido\" o \"tierra de clima caliente\". Era un nombre " +
                        "geográfico impuesto desde afuera: ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("los que viven en el calor") }
                    append(".")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun MochicaCard(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        HorizontalDivider(thickness = 4.dp, color = Ocean)
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "2. La derivación: Mochica",
                style = MaterialTheme.typography.titleMedium,
                color = Ocean,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Text(
                text = buildAnnotatedString {
                    append("Cuando los colonos españoles se asentaron en la región, les costaba pronunciar " +
                        "los sonidos exactos de los nativos. Escuchaban el término originario \"Muchik\" y lo " +
                        "asociaron rápidamente al nombre del valle principal que hoy conocemos como ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Moche") }
                    append(". Al castellanizar la palabra, le añadieron la \"a\" al final, creando el término ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("\"Mochica\"") }
                    append(".")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun EtymologyCard(isWide: Boolean) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Gold, MaterialTheme.shapes.large),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "La Autodenominación Verdadera",
                style = MaterialTheme.typography.headlineSmall,
                color = DarkClay,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("\"Muchik\"") }
                    append(" es cómo los propios hablantes originarios llamaban a su lengua y a su cultura. " +
                        "Según los análisis lingüísticos de documentos históricos (como la Lista del Cónsul " +
                        "Montjoy de 1865 y los registros de F. de la Carrera de 1644), la palabra se compone " +
                        "de una raíz y un sufijo sumamente reveladores:")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp).padding(horizontal = 8.dp),
            )

            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RootBox(modifier = Modifier.weight(1f, fill = false))
                    Text(text = "+", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.outlineVariant, fontWeight = FontWeight.Bold)
                    SuffixBox(modifier = Modifier.weight(1f, fill = false))
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    RootBox(modifier = Modifier.fillMaxWidth())
                    Text(text = "+", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.outlineVariant, fontWeight = FontWeight.Bold)
                    SuffixBox(modifier = Modifier.fillMaxWidth())
                }
            }

            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Sand.copy(alpha = 0.4f), MaterialTheme.shapes.medium)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Al unir estas piezas, el consenso de los investigadores nos revela que \"Muchik\" significa literal y poéticamente:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Text(
                    text = "\"Lo nuestro\"",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Clay,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "o \"El habla de los nuestros\"",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkClay,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun RootBox(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Sand.copy(alpha = 0.5f), MaterialTheme.shapes.medium)
            .border(1.dp, Sand, MaterialTheme.shapes.medium)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Much / Mæich",
            style = MaterialTheme.typography.titleLarge,
            color = Clay,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "LA RAÍZ: \"NOSOTROS\"",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = androidx.compose.ui.unit.TextUnit(2f, androidx.compose.ui.unit.TextUnitType.Sp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SuffixBox(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Sand.copy(alpha = 0.5f), MaterialTheme.shapes.medium)
            .border(1.dp, Sand, MaterialTheme.shapes.medium)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "-ik / -c",
            style = MaterialTheme.typography.titleLarge,
            color = Clay,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "EL SUFIJO: \"PERTENENCIA\"",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = androidx.compose.ui.unit.TextUnit(2f, androidx.compose.ui.unit.TextUnitType.Sp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun IdentityQuote() {
    Text(
        text = "Decir \"Yo hablo Muchik\" es, en la mente de nuestros ancestros, " +
            "una forma de decir: \"Yo hablo lo nuestro\". Una declaración eterna de identidad.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}
