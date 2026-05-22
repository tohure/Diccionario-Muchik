package dev.tohure.muchik_dictionary.core.di

import dev.tohure.muchik_dictionary.feature.dictionary.di.dictionaryModule
import dev.tohure.muchik_dictionary.feature.numbers.di.numbersModule
import dev.tohure.muchik_dictionary.feature.quiz.di.quizModule
import dev.tohure.muchik_dictionary.feature.sync.di.syncModule
import org.koin.core.module.Module
import org.koin.dsl.module

fun appModule(platformModule: Module): Module = module {
    includes(platformModule, dictionaryModule, quizModule, numbersModule, syncModule)
}
