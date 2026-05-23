package dev.tohure.muchik_dictionary.feature.dictionary.data.repository

import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordEntry
import dev.tohure.muchik_dictionary.feature.dictionary.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// Implementación en memoria sobre STATIC_ENTRIES. Usada por web (js/wasmJs) y tests.
// Android, iOS y Desktop usan RoomDictionaryRepositoryImpl vía platformModule.
class DictionaryRepositoryImpl : DictionaryRepository {

    private val allEntries: List<WordEntry> = STATIC_ENTRIES

    override fun observeAll(): Flow<List<WordEntry>> =
        flowOf(allEntries.sortedBy { it.muchikTerm })

    override fun search(query: String): Flow<List<WordEntry>> {
        if (query.isBlank()) return observeAll()
        val q = query.trim().lowercase()
        return flowOf(
            allEntries.filter { entry ->
                entry.muchikTerm.lowercase().contains(q) ||
                    entry.spanishTranslation.lowercase().contains(q) ||
                    entry.etymologyNote.lowercase().contains(q)
            }.sortedBy { it.muchikTerm }
        )
    }

    override fun observeByCategory(category: String): Flow<List<WordEntry>> =
        flowOf(allEntries.filter { it.category == category }.sortedBy { it.muchikTerm })

    override suspend fun getCategoryCounts(): Map<String, Int> =
        allEntries.groupBy { it.category }.mapValues { it.value.size }

    override suspend fun count(): Int = allEntries.size
}
