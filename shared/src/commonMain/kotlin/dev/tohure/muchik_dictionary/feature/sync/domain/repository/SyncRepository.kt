package dev.tohure.muchik_dictionary.feature.sync.domain.repository

import kotlinx.coroutines.flow.Flow

interface SyncRepository {
    fun syncFlow(): Flow<SyncResult>
    fun deltaSyncFlow(): Flow<SyncResult>
}

sealed class SyncResult {
    data object HasLocalData : SyncResult()
    data object Syncing : SyncResult()
    data class Done(val newEntries: Int) : SyncResult()
    data class Error(val message: String) : SyncResult()
}
