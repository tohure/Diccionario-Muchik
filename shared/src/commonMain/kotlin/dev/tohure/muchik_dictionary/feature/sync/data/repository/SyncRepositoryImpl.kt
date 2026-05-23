package dev.tohure.muchik_dictionary.feature.sync.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.tohure.muchik_dictionary.core.database.DictionaryDao
import dev.tohure.muchik_dictionary.core.database.toEntity
import dev.tohure.muchik_dictionary.feature.dictionary.data.repository.STATIC_ENTRIES
import dev.tohure.muchik_dictionary.feature.sync.data.remote.SyncApiService
import dev.tohure.muchik_dictionary.feature.sync.data.remote.toEntity
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncRepository
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

private val LAST_SYNC_KEY = stringPreferencesKey("last_sync_at")

class SyncRepositoryImpl(
    private val api: SyncApiService,
    private val dao: DictionaryDao,
    private val dataStore: DataStore<Preferences>,
) : SyncRepository {

    override fun syncFlow(): Flow<SyncResult> = flow {
        val localCount = dao.count()
        val lastSyncAt = dataStore.data.first()[LAST_SYNC_KEY]

        if (localCount > 0L) {
            // Data already available — let the app start immediately
            emit(SyncResult.HasLocalData)
        } else {
            // First launch — user must wait for the corpus to download
            emit(SyncResult.Syncing)
        }

        try {
            val entries = if (lastSyncAt == null || localCount == 0L) {
                api.fetchAll()
            } else {
                api.fetchSince(lastSyncAt)
            }

            if (entries.isNotEmpty()) {
                dao.insertAll(entries.map { it.toEntity() })
                val maxUpdatedAt = entries.maxOf { it.updatedAt }
                dataStore.edit { it[LAST_SYNC_KEY] = maxUpdatedAt }
            }

            if (localCount == 0L) {
                emit(SyncResult.Done(entries.size))
            }
        } catch (e: Exception) {
            if (localCount == 0L) {
                // Sin datos locales y sin red: cargamos el corpus estático embebido
                dao.insertAll(STATIC_ENTRIES.map { it.toEntity() })
                dataStore.edit { it[LAST_SYNC_KEY] = STATIC_ENTRIES.maxOf { it.updatedAt } }
                emit(SyncResult.Done(STATIC_ENTRIES.size))
            }
            // Si hay datos locales, ignoramos el error (offline-first)
        }
    }

    override fun deltaSyncFlow(): Flow<SyncResult> = flow {
        emit(SyncResult.Syncing)
        try {
            val lastSyncAt = dataStore.data.first()[LAST_SYNC_KEY]
            val entries = if (lastSyncAt != null) api.fetchSince(lastSyncAt) else api.fetchAll()
            if (entries.isNotEmpty()) {
                dao.insertAll(entries.map { it.toEntity() })
                dataStore.edit { it[LAST_SYNC_KEY] = entries.maxOf { it.updatedAt } }
            }
            emit(SyncResult.Done(entries.size))
        } catch (e: Exception) {
            emit(SyncResult.Error(e.message ?: "Error de red"))
        }
    }
}
