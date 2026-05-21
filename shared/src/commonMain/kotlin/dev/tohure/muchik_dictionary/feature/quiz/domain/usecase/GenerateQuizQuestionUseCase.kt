package dev.tohure.muchik_dictionary.feature.quiz.domain.usecase

import dev.tohure.muchik_dictionary.feature.dictionary.domain.repository.DictionaryRepository
import dev.tohure.muchik_dictionary.feature.quiz.domain.model.QuizQuestion
import kotlinx.coroutines.flow.first

class GenerateQuizQuestionUseCase(private val repository: DictionaryRepository) {

    suspend operator fun invoke(): QuizQuestion? {
        val all = repository.observeAll().first()
        val playable = all.filter { "clasificador" !in it.spanishTranslation.lowercase() }
        if (playable.size < 4) return null

        val question = playable.random()
        val distractors = (playable - question)
            .shuffled()
            .take(3)
            .map { it.spanishTranslation }

        val options = (distractors + question.spanishTranslation).shuffled()

        return QuizQuestion(
            muchikTerm = question.muchikTerm,
            emoji = question.emoji,
            correctAnswer = question.spanishTranslation,
            options = options,
        )
    }
}
