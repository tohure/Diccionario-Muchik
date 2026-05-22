package dev.tohure.muchik_dictionary.core.di

import dev.tohure.muchik_dictionary.feature.dictionary.data.repository.DictionaryRepositoryImpl
import dev.tohure.muchik_dictionary.feature.dictionary.domain.repository.DictionaryRepository
import dev.tohure.muchik_dictionary.feature.sync.data.repository.NoOpSyncRepository
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val platformModule: Module = module {
    single<DictionaryRepository> { DictionaryRepositoryImpl() }
    single<SyncRepository> { NoOpSyncRepository() }
}
