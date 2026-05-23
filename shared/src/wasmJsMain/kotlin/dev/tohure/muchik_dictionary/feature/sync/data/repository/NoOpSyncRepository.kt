package dev.tohure.muchik_dictionary.feature.sync.data.repository

import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncRepository
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NoOpSyncRepository : SyncRepository {
    override fun syncFlow(): Flow<SyncResult> = flowOf(SyncResult.HasLocalData)

    override fun deltaSyncFlow(): Flow<SyncResult> = flowOf(SyncResult.Done(0))
}
