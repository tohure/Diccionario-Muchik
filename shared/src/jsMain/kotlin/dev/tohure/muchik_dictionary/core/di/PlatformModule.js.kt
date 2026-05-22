package dev.tohure.muchik_dictionary.core.di

import dev.tohure.muchik_dictionary.feature.dictionary.data.repository.DictionaryRepositoryImpl
import dev.tohure.muchik_dictionary.feature.dictionary.domain.repository.DictionaryRepository
import dev.tohure.muchik_dictionary.feature.sync.data.repository.NoOpSyncRepository
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncRepository
import org.koin.core.module.Module
import org.koin.dsl.module

// JS no tiene Room ni Ktor-CIO disponible; usa datos estáticos y sync desactivado.
// NoOpSyncRepository retorna Done(0) sin llamadas de red para no romper la UI de sincronización.
val platformModule: Module = module {
    single<DictionaryRepository> { DictionaryRepositoryImpl() }
    single<SyncRepository> { NoOpSyncRepository() }
}
