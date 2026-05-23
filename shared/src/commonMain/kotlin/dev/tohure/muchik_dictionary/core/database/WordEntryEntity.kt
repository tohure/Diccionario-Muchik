package dev.tohure.muchik_dictionary.core.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordEntry

@Entity(tableName = "word_entry")
data class WordEntryEntity(
    @PrimaryKey val id: String,
    val muchikTerm: String,
    val spanishTranslation: String,
    val category: String,
    val emoji: String,
    val etymologyNote: String,
    val sourceReference: String,
    val updatedAt: String,
)

fun WordEntryEntity.toDomain() = WordEntry(
    id = id,
    muchikTerm = muchikTerm,
    spanishTranslation = spanishTranslation,
    category = category,
    emoji = emoji,
    etymologyNote = etymologyNote,
    sourceReference = sourceReference,
    updatedAt = updatedAt,
)

fun WordEntry.toEntity() = WordEntryEntity(
    id = id,
    muchikTerm = muchikTerm,
    spanishTranslation = spanishTranslation,
    category = category,
    emoji = emoji,
    etymologyNote = etymologyNote,
    sourceReference = sourceReference,
    updatedAt = updatedAt,
)
