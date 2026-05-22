package dev.tohure.muchik_dictionary.feature.dictionary.data.repository

import dev.tohure.muchik_dictionary.core.database.DictionaryDao
import dev.tohure.muchik_dictionary.core.database.toDomain
import dev.tohure.muchik_dictionary.core.database.toEntity
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordEntry
import dev.tohure.muchik_dictionary.feature.dictionary.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RoomDictionaryRepositoryImpl(private val dao: DictionaryDao) : DictionaryRepository {

    private var seeded = false

    private suspend fun ensureSeeded() {
        if (!seeded && dao.count() == 0L) {
            dao.insertAll(STATIC_ENTRIES.map { it.toEntity() })
        }
        seeded = true
    }

    override fun observeAll(): Flow<List<WordEntry>> = flow {
        ensureSeeded()
        dao.observeAll().collect { emit(it.map { e -> e.toDomain() }) }
    }

    override fun search(query: String): Flow<List<WordEntry>> {
        if (query.isBlank()) return observeAll()
        val ftsQuery = query.trim().split(" ")
            .joinToString(" ") { "$it*" }
        return dao.searchFts(ftsQuery).map { list -> list.map { it.toDomain() } }
    }

    override fun observeByCategory(category: String): Flow<List<WordEntry>> =
        dao.observeByCategory(category).map { list -> list.map { it.toDomain() } }

    override suspend fun getCategoryCounts(): Map<String, Int> {
        ensureSeeded()
        return dao.getCategoryCounts().associate { it.category to it.cnt }
    }

    override suspend fun count(): Int {
        ensureSeeded()
        return dao.count().toInt()
    }
}
