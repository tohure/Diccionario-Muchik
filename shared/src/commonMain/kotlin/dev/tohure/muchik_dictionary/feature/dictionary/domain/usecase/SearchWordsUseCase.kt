package dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase

import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordEntry
import dev.tohure.muchik_dictionary.feature.dictionary.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow

class SearchWordsUseCase(
    private val repository: DictionaryRepository,
) {
    operator fun invoke(query: String): Flow<List<WordEntry>> = repository.search(query)
}
