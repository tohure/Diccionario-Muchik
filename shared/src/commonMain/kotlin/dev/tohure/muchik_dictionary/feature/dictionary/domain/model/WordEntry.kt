package dev.tohure.muchik_dictionary.feature.dictionary.domain.model

data class WordEntry(
    val id: String,
    val muchikTerm: String,
    val spanishTranslation: String,
    val category: String,
    val emoji: String,
    val etymologyNote: String,
    val sourceReference: String,
    val updatedAt: String,
)
