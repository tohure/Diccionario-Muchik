package dev.tohure.muchik_dictionary.core.di

import dev.tohure.muchik_dictionary.feature.dictionary.di.dictionaryModule
import dev.tohure.muchik_dictionary.feature.numbers.di.numbersModule
import dev.tohure.muchik_dictionary.feature.quiz.di.quizModule
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    includes(dictionaryModule, quizModule, numbersModule)
}
