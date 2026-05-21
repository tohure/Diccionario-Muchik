package dev.tohure.muchik_dictionary.feature.quiz.presentation.state

import dev.tohure.muchik_dictionary.feature.quiz.domain.model.QuizQuestion

data class QuizUiState(
    val isLoading: Boolean = true,
    val question: QuizQuestion? = null,
    val selectedOption: String? = null,
    val isAnswered: Boolean = false,
    val isCorrect: Boolean? = null,
    val score: Int = 0,
)
