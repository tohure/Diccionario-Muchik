package dev.tohure.muchik_dictionary.feature.quiz.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class QuizOptionState { Default, Correct, Incorrect, Disabled }

@Composable
fun QuizOptionButton(
    text: String,
    state: QuizOptionState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = when (state) {
        QuizOptionState.Correct -> Color(0xFFD1FAE5)
        QuizOptionState.Incorrect -> Color(0xFFFEE2E2)
        QuizOptionState.Disabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        QuizOptionState.Default -> Color.White
    }
    val borderColor = when (state) {
        QuizOptionState.Correct -> Color(0xFF10B981)
        QuizOptionState.Incorrect -> Color(0xFFEF4444)
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val textColor = when (state) {
        QuizOptionState.Correct -> Color(0xFF065F46)
        QuizOptionState.Incorrect -> Color(0xFF991B1B)
        QuizOptionState.Disabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
        QuizOptionState.Default -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .then(
                if (state == QuizOptionState.Default) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
        )
    }
}
