package dev.tohure.muchik_dictionary.feature.credits.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Gold
import dev.tohure.muchik_dictionary.core.design.Ocean
import dev.tohure.muchik_dictionary.core.design.Sand

private enum class SourceStyle { NORMAL, HIGHLIGHT_BLUE, HIGHLIGHT_GREEN }

private data class SourceEntry(
    val author: String,
    val description: String,
    val style: SourceStyle = SourceStyle.NORMAL,
)

private val historicalSources = listOf(
    SourceEntry(
        "Fernando de la Carrera Daza (1644):",
        " Su \"Arte de la lengua yunga...\" es la piedra angular y la gramática madre del idioma.",
    ),
    SourceEntry(
        "Santiago C. Montjoy (1865):",
        " Cónsul estadounidense cuya valiosa lista de palabras de Eten permaneció oculta un siglo.",
    ),
    SourceEntry(
        "Ernst W. Middendorf (1892):",
        " Primer gran investigador de la lingüística costeña.",
    ),
    SourceEntry(
        "Hans Heinrich Brüning (1924):",
        " Etno-lingüista que convivió con los últimos hablantes de Eten.",
    ),
    SourceEntry(
        "Federico Villarreal (1921):",
        " Recopiló vocabularios cruciales de informantes etenanos como don Amadeo Vilches.",
    ),
)

private val modernSources = listOf(
    SourceEntry(
        "Rodolfo Cerrón-Palomino y José A. Salas García:",
        " Máximas autoridades en la reconstrucción fonológica y diccionarios modernos.",
    ),
    SourceEntry(
        "Rita Eloranta (2025):",
        " Sus manuales han corregido errores históricos de sufijos y clarificado el sistema inalienable.",
    ),
    SourceEntry(
        "Docentes USS (2012-2014):",
        " Juan Carlos Chero Zurita, Medali Peralta Vallejos y el arqueólogo Luis Enrique Chero Zurita. " +
            "Descifradores del brillante sistema matemático Moche (Tük muchik).",
    ),
    SourceEntry(
        "Universidad Nacional de Trujillo (UNT):",
        " Un profundo reconocimiento a los estudiantes de la Promoción XXVIII de Lengua y Literatura, " +
            "y a su docente Maribel Pizarro Mostacero, por su labor de difusión y revalorización en redes sociales.",
        style = SourceStyle.HIGHLIGHT_BLUE,
    ),
    SourceEntry(
        "Instituto Pedagógico Internacional Elim:",
        " Agradecimiento por los valiosos diccionarios etnolingüísticos recopilados por las investigadoras " +
            "Luz Saraí Ramos Carlos, Rosa Pérez Samillán, Cesia Román Pasapera y Estrella Castillo Rodríguez, " +
            "asegurando el futuro del idioma en las aulas.",
        style = SourceStyle.HIGHLIGHT_GREEN,
    ),
)

@Composable
fun CreditsScreen() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWide = maxWidth > 700.dp
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Fuentes y Agradecimientos",
                        style = MaterialTheme.typography.headlineMedium,
                        color = DarkClay,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Este diccionario es el resultado monumental del esfuerzo de siglos de " +
                            "curas, lingüistas, y, sobre todo, la inquebrantable memoria de nuestros " +
                            "pueblos y la nueva ola de estudiantes universitarios.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }
            }

            item {
                if (isWide) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        HistoricalCard(modifier = Modifier.weight(1f))
                        ModernCard(modifier = Modifier.weight(1f))
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        HistoricalCard(modifier = Modifier.fillMaxWidth())
                        ModernCard(modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            item { CommunitySection() }
        }
    }
}

@Composable
private fun HistoricalCard(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Top border — Clay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Clay),
            )
            // Watermark emoji
            Text(
                text = "📜",
                fontSize = 80.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .then(Modifier),
                color = Color.Black.copy(alpha = 0.05f),
            )
            Column(modifier = Modifier.padding(top = 4.dp).padding(20.dp)) {
                Text(
                    text = "✒️  Archivos Históricos y Coloniales",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkClay,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    historicalSources.forEach { source ->
                        SourceItem(
                            source = source,
                            normalBorderColor = Clay.copy(alpha = 0.35f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernCard(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Ocean),
            )
            Text(
                text = "🔬",
                fontSize = 80.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp),
                color = Color.Black.copy(alpha = 0.05f),
            )
            Column(modifier = Modifier.padding(top = 4.dp).padding(20.dp)) {
                Text(
                    text = "📚  Investigación y Revitalización Actual",
                    style = MaterialTheme.typography.titleMedium,
                    color = Ocean,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    modernSources.forEach { source ->
                        SourceItem(
                            source = source,
                            normalBorderColor = Ocean.copy(alpha = 0.35f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SourceItem(source: SourceEntry, normalBorderColor: Color) {
    val (borderColor, bgColor, borderWidth) = when (source.style) {
        SourceStyle.NORMAL -> Triple(normalBorderColor, Color.Transparent, 2.dp)
        SourceStyle.HIGHLIGHT_BLUE -> Triple(Color(0xFF60A5FA), Color(0xFFEFF6FF), 4.dp)
        SourceStyle.HIGHLIGHT_GREEN -> Triple(Color(0xFF34D399), Color(0xFFF0FDF4), 4.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(bgColor, MaterialTheme.shapes.small),
    ) {
        Box(
            modifier = Modifier
                .width(borderWidth)
                .fillMaxHeight()
                .background(borderColor),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(source.author)
                }
                append(source.description)
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp, end = 4.dp),
        )
    }
}

@Composable
private fun CommunitySection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFBEB), MaterialTheme.shapes.extraLarge)
            .then(
                Modifier.background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(Color(0xFFFFFBEB), Color(0xFFFFF3E0)),
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
                )
            )
            .border(1.dp, Gold, MaterialTheme.shapes.extraLarge)
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "🫂 🌱",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Mención de Honor a las Comunidades",
                style = MaterialTheme.typography.headlineSmall,
                color = DarkClay,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
            )
            Text(
                text = buildAnnotatedString {
                    append("Nada de esta erudición tendría sentido sin la sangre viva que mantuvo " +
                        "latiendo estas palabras. Nuestro tributo imperecedero a las herederas de ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Clay)) {
                        append("Ciudad Eten, Monsefú, Mórrope y Lambayeque")
                    }
                    append(".")
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = buildAnnotatedString {
                    append("A las honorables familias ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Quesquén, Nuntón, Isique, Ayasta, Chafloque y Llonto")
                    }
                    append("; y en memoria de don ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("Simón Quesquén") }
                    append(", cuyas grabaciones nos devolvieron la voz de los ancestros. Ellos " +
                        "resguardaron el idioma en el calor de los fogones (")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("Tok") }
                    append("), en el tejido (")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("Jem") }
                    append(") y en sus memorias frente al paso arrollador de los siglos.")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.6f), MaterialTheme.shapes.medium)
                    .padding(16.dp),
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Un abrazo fraterno a todos los ")
                        withStyle(SpanStyle(color = Ocean, fontWeight = FontWeight.SemiBold)) {
                            append("alumnos, profesores y gestores culturales")
                        }
                        append(" que hoy enarbolan la identidad ")
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("Iñikuk") }
                        append(" y levantan el movimiento ")
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("NeoMochica") }
                        append(". ¡El idioma vive en ustedes!")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
