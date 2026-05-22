package dev.tohure.muchik_dictionary.feature.contact.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Sand

private const val CONTACT_EMAIL = "cr.htorres@gmail.com"

@Composable
fun ContactScreen() {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(8.dp))
        ElevatedCard(
            modifier = Modifier.widthIn(max = 720.dp).fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            HorizontalDivider(thickness = 8.dp, color = Clay)
            Column(
                modifier = Modifier.fillMaxWidth().padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "¡Escríbeme!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = DarkClay,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Construyamos juntos nuestra identidad.",
                    style = MaterialTheme.typography.titleMedium,
                    color = Clay,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                Text(
                    text = buildAnnotatedString {
                        append("¡")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Peño Tæsæk") }
                        append(" a este espacio colaborativo! Este diccionario es un ente vivo, " +
                            "igual que nuestra herencia Moche, y requiere del cuidado de todos nosotros " +
                            "para crecer fuerte y preciso.")
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = buildAnnotatedString {
                        append("Mi ojo no siempre es perfecto. Si al navegar por estas páginas identificas ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("problemas con la fonética") }
                        append(", crees que alguna palabra pertenece a otra ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("categorización") }
                        append(", o si simplemente has encontrado un viejo apunte, un libro o un recuerdo " +
                            "familiar que pueda aportar nuevos vocablos...")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Sand.copy(alpha = 0.5f), MaterialTheme.shapes.large)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "POR FAVOR, ENVÍAME UN MENSAJE A:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = androidx.compose.ui.unit.TextUnit(
                            value = 1.5f,
                            type = androidx.compose.ui.unit.TextUnitType.Sp,
                        ),
                        textAlign = TextAlign.Center,
                    )
                    Button(
                        onClick = { uriHandler.openUri("mailto:$CONTACT_EMAIL") },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkClay,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        modifier = Modifier.widthIn(min = 200.dp),
                    ) {
                        Text(
                            text = "📧  $CONTACT_EMAIL",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                        )
                    }
                }

                Text(
                    text = "Toda nueva ayuda, crítica constructiva o documento es bienvenida. " +
                        "¡Hagamos que el Muchik vuelva a resonar!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
