package dev.tohure.muchik_dictionary.feature.sync.di

import dev.tohure.muchik_dictionary.feature.sync.presentation.viewmodel.SyncViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val syncModule: Module =
    module {
        viewModel { SyncViewModel(get()) }
    }
