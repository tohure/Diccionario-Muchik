package dev.tohure.muchik_dictionary.feature.numbers.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Ocean
import dev.tohure.muchik_dictionary.core.design.OffWhite
import dev.tohure.muchik_dictionary.core.design.Sand
import dev.tohure.muchik_dictionary.feature.numbers.domain.model.NumberEntry

@Composable
fun NumberCounter(
    entry: NumberEntry,
    canDecrement: Boolean,
    canIncrement: Boolean,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Sand.copy(alpha = 0.3f), MaterialTheme.shapes.large)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CounterButton(
                label = "−",
                enabled = canDecrement,
                color = Clay,
                onClick = onDecrement,
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = entry.value?.toString() ?: "∞",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    color = DarkClay,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = entry.muchikTerm,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Serif,
                )
                Text(
                    text = entry.spanishTranslation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }

            CounterButton(
                label = "+",
                enabled = canIncrement,
                color = Ocean,
                onClick = onIncrement,
            )
        }
    }
}

@Composable
private fun CounterButton(
    label: String,
    enabled: Boolean,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    val bg = if (enabled) OffWhite else OffWhite.copy(alpha = 0.5f)
    val fg = if (enabled) color else color.copy(alpha = 0.3f)

    Box(
        modifier = Modifier
            .size(56.dp)
            .shadow(if (enabled) 4.dp else 0.dp, CircleShape)
            .clip(CircleShape)
            .background(bg)
            .border(1.dp, fg.copy(alpha = 0.4f), CircleShape)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = fg,
        )
    }
}
