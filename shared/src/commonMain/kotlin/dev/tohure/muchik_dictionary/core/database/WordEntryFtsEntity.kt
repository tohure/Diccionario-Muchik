package dev.tohure.muchik_dictionary.core.database

import androidx.room3.Entity
import androidx.room3.Fts5

@Fts5(contentEntity = WordEntryEntity::class)
@Entity(tableName = "word_entry_fts")
data class WordEntryFtsEntity(
    val muchikTerm: String,
    val spanishTranslation: String,
    val etymologyNote: String,
)
