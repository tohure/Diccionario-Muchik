package dev.tohure.muchik_dictionary.feature.numbers.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dev.tohure.muchik_dictionary.feature.numbers.presentation.state.NumbersUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NumbersViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NumbersUiState())
    val uiState: StateFlow<NumbersUiState> = _uiState.asStateFlow()

    fun onIncrement() {
        _uiState.update { state ->
            if (state.canIncrement) state.copy(currentIndex = state.currentIndex + 1) else state
        }
    }

    fun onDecrement() {
        _uiState.update { state ->
            if (state.canDecrement) state.copy(currentIndex = state.currentIndex - 1) else state
        }
    }
}
