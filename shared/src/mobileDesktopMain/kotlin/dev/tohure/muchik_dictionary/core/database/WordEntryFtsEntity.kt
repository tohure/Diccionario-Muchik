package dev.tohure.muchik_dictionary.core.database

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = WordEntryEntity::class)
@Entity(tableName = "word_entry_fts")
data class WordEntryFtsEntity(
    val muchikTerm: String,
    val spanishTranslation: String,
    val etymologyNote: String,
)
