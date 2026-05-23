package dev.tohure.muchik_dictionary.feature.dictionary.di

import dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase.GetAllWordsUseCase
import dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase.GetCategoryCountsUseCase
import dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase.SearchWordsUseCase
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.viewmodel.DictionaryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dictionaryModule =
    module {
        factory { GetAllWordsUseCase(get()) }
        factory { SearchWordsUseCase(get()) }
        factory { GetCategoryCountsUseCase(get()) }
        viewModel { DictionaryViewModel(get(), get(), get()) }
    }
