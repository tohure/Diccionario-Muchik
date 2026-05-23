package dev.tohure.muchik_dictionary.feature.quiz.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.LocalEmojiFontFamily
import dev.tohure.muchik_dictionary.core.design.Ocean
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import dictionarymuchik.shared.generated.resources.Res
import dictionarymuchik.shared.generated.resources.a11y_loading_quiz
import dictionarymuchik.shared.generated.resources.quiz_correct
import dictionarymuchik.shared.generated.resources.quiz_incorrect_prefix
import dictionarymuchik.shared.generated.resources.quiz_next_button
import dictionarymuchik.shared.generated.resources.quiz_score_label
import dictionarymuchik.shared.generated.resources.quiz_term_label
import org.jetbrains.compose.resources.stringResource
import dev.tohure.muchik_dictionary.feature.quiz.domain.model.QuizQuestion
import dev.tohure.muchik_dictionary.feature.quiz.presentation.state.QuizUiState
import dev.tohure.muchik_dictionary.feature.quiz.presentation.viewmodel.QuizViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuizScreen(viewModel: QuizViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()

    QuizContent(
        state = state,
        onOptionSelected = viewModel::onOptionSelected,
        onNextQuestion = viewModel::loadNextQuestion,
    )
}

@Composable
private fun QuizContent(
    state: QuizUiState,
    onOptionSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        QuizHeader(
            question = state.question,
            isLoading = state.isLoading,
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (state.isLoading || state.question == null) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                RoundedCornerShape(12.dp),
                            ),
                    )
                }
            } else {
                state.question.options.forEach { option ->
                    QuizOptionButton(
                        text = option,
                        state = optionStateFor(option, state),
                        onClick = { onOptionSelected(option) },
                    )
                }
            }
        }

        if (state.isAnswered && state.question != null) {
            QuizFeedback(
                isCorrect = state.isCorrect == true,
                correctAnswer = state.question.correctAnswer,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            Spacer(Modifier.height(8.dp))
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = if (state.isAnswered) 16.dp else 0.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        QuizFooter(
            score = state.score,
            onNextQuestion = onNextQuestion,
        )
    }
}

@Composable
private fun QuizHeader(
    question: QuizQuestion?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    val emojiFont = LocalEmojiFontFamily.current
    val loadingDesc = stringResource(Res.string.a11y_loading_quiz)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(DarkClay, Clay)))
            .padding(24.dp),
    ) {
        if (!isLoading && question != null && question.emoji.isNotBlank()) {
            Text(
                text = question.emoji,
                fontSize = 84.sp,
                fontFamily = emojiFont,
                color = Color.White.copy(alpha = 0.20f),
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }

        Column {
            Box(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.20f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    text = stringResource(Res.string.quiz_term_label),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(12.dp))

            if (isLoading || question == null) {
                CircularProgressIndicator(
                    color = Color.White.copy(alpha = 0.7f),
                    strokeWidth = 2.dp,
                    modifier = Modifier.padding(vertical = 8.dp).semantics { contentDescription = loadingDesc },
                )
            } else {
                Text(
                    text = question.muchikTerm,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun QuizFeedback(
    isCorrect: Boolean,
    correctAnswer: String,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (isCorrect) Color(0xFFECFDF5) else Color(0xFFFEF2F2)
    val borderColor = if (isCorrect) Color(0xFF6EE7B7) else Color(0xFFFCA5A5)
    val textColor = if (isCorrect) Color(0xFF065F46) else Color(0xFF991B1B)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isCorrect) {
            Text(
                text = stringResource(Res.string.quiz_correct),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        } else {
            Text(
                text = stringResource(Res.string.quiz_incorrect_prefix),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = correctAnswer,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun QuizFooter(
    score: Int,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.quiz_score_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Box(
                modifier = Modifier
                    .background(Color(0xFFCCFBF1), CircleShape)
                    .border(1.dp, Color(0xFF99F6E4), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 4.dp),
            ) {
                Text(
                    text = score.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Ocean,
                )
            }
        }

        Button(
            onClick = onNextQuestion,
            colors = ButtonDefaults.buttonColors(containerColor = Clay),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.quiz_next_button),
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }
    }
}

private fun optionStateFor(option: String, state: QuizUiState): QuizOptionState {
    if (!state.isAnswered) return QuizOptionState.Default
    return when {
        option == state.selectedOption && state.isCorrect == true -> QuizOptionState.Correct
        option == state.selectedOption && state.isCorrect == false -> QuizOptionState.Incorrect
        else -> QuizOptionState.Disabled
    }
}
