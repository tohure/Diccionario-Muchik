@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package dev.tohure.muchik_dictionary.core.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import dev.tohure.muchik_dictionary.core.database.AppDatabase
import dev.tohure.muchik_dictionary.core.database.getDatabaseBuilder
import dev.tohure.muchik_dictionary.core.database.getRoomDatabase
import dev.tohure.muchik_dictionary.feature.dictionary.data.repository.RoomDictionaryRepositoryImpl
import dev.tohure.muchik_dictionary.feature.dictionary.domain.repository.DictionaryRepository
import dev.tohure.muchik_dictionary.feature.sync.data.remote.SyncApiService
import dev.tohure.muchik_dictionary.feature.sync.data.repository.SyncRepositoryImpl
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

val platformModule: Module = module {
    single { getRoomDatabase(getDatabaseBuilder()) }
    single { get<AppDatabase>().dictionaryDao() }
    single<DictionaryRepository> { RoomDictionaryRepositoryImpl(get()) }
    single {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        val path = requireNotNull(documentDirectory?.path) + "/muchik_sync.preferences_pb"
        PreferenceDataStoreFactory.createWithPath(produceFile = { path.toPath() })
    }
    single {
        HttpClient(Darwin) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }
    }
    single { SyncApiService(get()) }
    single<SyncRepository> { SyncRepositoryImpl(get(), get(), get()) }
}
