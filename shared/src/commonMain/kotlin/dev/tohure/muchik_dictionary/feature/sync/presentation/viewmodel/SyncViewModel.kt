package dev.tohure.muchik_dictionary.feature.sync.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncRepository
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncResult
import dev.tohure.muchik_dictionary.feature.sync.presentation.state.SyncUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SyncViewModel(
    private val syncRepository: SyncRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SyncUiState>(SyncUiState.Loading)
    val uiState: StateFlow<SyncUiState> = _uiState.asStateFlow()

    init {
        startSync()
    }

    private fun startSync() {
        _uiState.value = SyncUiState.Loading
        viewModelScope.launch {
            syncRepository
                .syncFlow()
                .catch { e -> _uiState.value = SyncUiState.Error(e.message ?: "Error de sincronización") }
                .collect { result ->
                    when (result) {
                        SyncResult.HasLocalData -> _uiState.value = SyncUiState.Done
                        SyncResult.Syncing -> _uiState.value = SyncUiState.Loading
                        is SyncResult.Done -> _uiState.value = SyncUiState.Done
                        is SyncResult.Error -> _uiState.value = SyncUiState.Error(result.message)
                    }
                }
        }
    }

    fun retry() = startSync()
}
