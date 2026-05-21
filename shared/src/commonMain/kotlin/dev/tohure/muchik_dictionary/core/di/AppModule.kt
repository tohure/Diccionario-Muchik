package dev.tohure.muchik_dictionary.core.di

import dev.tohure.muchik_dictionary.feature.dictionary.di.dictionaryModule
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    includes(dictionaryModule)
}
