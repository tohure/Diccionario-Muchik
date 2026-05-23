package dev.tohure.muchik_dictionary.feature.quiz.domain.model

data class QuizQuestion(
    val muchikTerm: String,
    val emoji: String,
    val correctAnswer: String,
    val options: List<String>,
)
