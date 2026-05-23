package dev.tohure.muchik_dictionary.feature.quiz.di

import dev.tohure.muchik_dictionary.feature.quiz.domain.usecase.GenerateQuizQuestionUseCase
import dev.tohure.muchik_dictionary.feature.quiz.presentation.viewmodel.QuizViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val quizModule =
    module {
        factory { GenerateQuizQuestionUseCase(get()) }
        viewModel { QuizViewModel(get()) }
    }
