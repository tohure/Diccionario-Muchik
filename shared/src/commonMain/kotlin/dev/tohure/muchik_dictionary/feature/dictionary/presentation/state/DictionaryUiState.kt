package dev.tohure.muchik_dictionary.feature.dictionary.presentation.state

import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordCategory
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordEntry

enum class DictionaryViewMode { CARDS, LIST }

data class DictionaryUiState(
    val isLoading: Boolean = true,
    val entries: List<WordEntry> = emptyList(),
    val query: String = "",
    val selectedCategory: WordCategory = WordCategory.ALL,
    val viewMode: DictionaryViewMode = DictionaryViewMode.CARDS,
    val totalCount: Int = 0,
    val categoryCounts: Map<String, Int> = emptyMap(),
    val errorMessage: String? = null,
)
