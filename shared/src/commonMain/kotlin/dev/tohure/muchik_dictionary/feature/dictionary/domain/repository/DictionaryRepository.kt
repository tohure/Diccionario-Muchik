package dev.tohure.muchik_dictionary.feature.dictionary.domain.repository

import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordEntry
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun observeAll(): Flow<List<WordEntry>>
    fun search(query: String): Flow<List<WordEntry>>
    fun observeByCategory(category: String): Flow<List<WordEntry>>
    suspend fun getCategoryCounts(): Map<String, Int>
    suspend fun count(): Int
}
