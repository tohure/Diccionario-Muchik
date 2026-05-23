package dev.tohure.muchik_dictionary.feature.numbers.di

import dev.tohure.muchik_dictionary.feature.numbers.presentation.viewmodel.NumbersViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val numbersModule = module {
    viewModel { NumbersViewModel() }
}
