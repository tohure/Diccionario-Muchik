package dev.tohure.muchik_dictionary.feature.sync.data.remote

import dev.tohure.muchik_dictionary.core.database.WordEntryEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WordEntryDto(
    val id: String,
    @SerialName("muchik_term") val muchikTerm: String,
    @SerialName("spanish_translation") val spanishTranslation: String,
    val category: String,
    val emoji: String,
    @SerialName("etymology_note") val etymologyNote: String,
    @SerialName("source_reference") val sourceReference: String,
    @SerialName("updated_at") val updatedAt: String,
)

fun WordEntryDto.toEntity() = WordEntryEntity(
    id = id,
    muchikTerm = muchikTerm,
    spanishTranslation = spanishTranslation,
    category = category,
    emoji = emoji,
    etymologyNote = etymologyNote,
    sourceReference = sourceReference,
    updatedAt = updatedAt,
)
