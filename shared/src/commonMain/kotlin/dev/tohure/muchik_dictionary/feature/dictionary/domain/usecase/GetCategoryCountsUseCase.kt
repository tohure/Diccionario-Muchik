package dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase

import dev.tohure.muchik_dictionary.feature.dictionary.domain.repository.DictionaryRepository

class GetCategoryCountsUseCase(private val repository: DictionaryRepository) {
    suspend operator fun invoke(): Map<String, Int> = repository.getCategoryCounts()
}
