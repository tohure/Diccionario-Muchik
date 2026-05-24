package dev.tohure.muchik_dictionary.feature.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.tohure.muchik_dictionary.feature.quiz.domain.usecase.GenerateQuizQuestionUseCase
import dev.tohure.muchik_dictionary.feature.quiz.presentation.state.QuizUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    private val generateQuestion: GenerateQuizQuestionUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadNextQuestion()
    }

    fun loadNextQuestion() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    selectedOption = null,
                    isAnswered = false,
                    isCorrect = null,
                )
            }
            val question = generateQuestion()
            _uiState.update { it.copy(isLoading = false, question = question) }
        }
    }

    fun onOptionSelected(option: String) {
        if (_uiState.value.isAnswered) return
        val question = _uiState.value.question ?: return
        val correct = option == question.correctAnswer
        _uiState.update {
            it.copy(
                selectedOption = option,
                isAnswered = true,
                isCorrect = correct,
                score = if (correct) it.score + 10 else it.score,
            )
        }
    }
}
