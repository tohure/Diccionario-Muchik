package dev.tohure.muchik_dictionary.feature.dictionary.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordCategory
import dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase.GetCategoryCountsUseCase
import dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase.SearchWordsUseCase
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.state.DictionaryUiState
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.state.DictionaryViewMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class DictionaryViewModel(
    private val searchWords: SearchWordsUseCase,
    private val getCategoryCounts: GetCategoryCountsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")
    private val categoryFlow = MutableStateFlow(WordCategory.ALL)

    init {
        loadCategoryCounts()
        observeEntries()
    }

    private fun loadCategoryCounts() {
        viewModelScope.launch {
            val counts = getCategoryCounts()
            _uiState.update { it.copy(totalCount = counts.values.sum(), categoryCounts = counts) }
        }
    }

    private fun observeEntries() {
        combine(queryFlow.debounce(150), categoryFlow) { query, category -> query to category }
            .flatMapLatest { (query, category) ->
                searchWords(query).map { entries ->
                    if (category == WordCategory.ALL) entries
                    else entries.filter { it.category == category.displayName }
                }
            }
            .onEach { filtered ->
                _uiState.update { it.copy(isLoading = false, entries = filtered, errorMessage = null) }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(query: String) {
        queryFlow.value = query
        _uiState.update { it.copy(query = query) }
    }

    fun onCategorySelected(category: WordCategory) {
        categoryFlow.value = category
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onViewModeToggle() {
        _uiState.update {
            it.copy(
                viewMode = if (it.viewMode == DictionaryViewMode.CARDS) DictionaryViewMode.LIST
                    else DictionaryViewMode.CARDS
            )
        }
    }
}
