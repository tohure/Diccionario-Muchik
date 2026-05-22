package dev.tohure.muchik_dictionary.feature.sync.presentation.state

sealed class SyncUiState {
    data object Loading : SyncUiState()
    data object Done : SyncUiState()
    data class Error(val message: String) : SyncUiState()
}
