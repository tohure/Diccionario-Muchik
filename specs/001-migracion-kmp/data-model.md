# Data Model: Migración KMP — Diccionario Muchik

**Branch**: `001-migracion-kmp` | **Date**: 2026-05-20

---

## Entities Overview

```
WordEntry (Domain)          WordEntryEntity (Room)       WordEntryFtsEntity (FTS4)
─────────────────           ──────────────────────       ──────────────────────────
id: String                  id: String (PK)              rowId: Int (PK)
muchikTerm: String          muchikTerm: String           muchikTerm: String
spanishTranslation: String  spanishTranslation: String   spanishTranslation: String
category: String            category: String             etymologyNote: String
emoji: String               emoji: String
etymologyNote: String       etymologyNote: String
sourceReference: String     sourceReference: String
updatedAt: Instant          updatedAt: String (ISO-8601)

SyncMetadata (DataStore Preferences — no table)
────────────────────────────────────────────────
SYNC_COMPLETED: Boolean
LAST_SYNC_DATE: String (ISO-8601)
```

---

## 1. WordEntry — Domain Model

```kotlin
// shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/feature/dictionary/domain/model/WordEntry.kt

data class WordEntry(
    val id: String,
    val muchikTerm: String,
    val spanishTranslation: String,
    val category: String,
    val emoji: String,
    val etymologyNote: String,
    val sourceReference: String,
    val updatedAt: String
)
```

**Constraints**:
- `muchikTerm`: no vacío, puede contener caracteres Unicode del Muchik (æ, ç, ñ, xll, tzh).
- `spanishTranslation`: no vacío.
- `category`: valor de enum `WordCategory` (ver abajo).
- `emoji`: carácter Unicode emoji, puede ser vacío en entradas sin representación visual.
- `sourceReference`: no vacío; si desconocido → `"TODO: verificar fuente primaria"`.
- `updatedAt`: ISO-8601 timestamp.

---

## 2. WordEntryEntity — Room Entity

```kotlin
// shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/feature/dictionary/data/local/entity/WordEntryEntity.kt

@Entity(tableName = "word_entries")
data class WordEntryEntity(
    @PrimaryKey val id: String,
    val muchikTerm: String,
    val spanishTranslation: String,
    val category: String,
    val emoji: String,
    val etymologyNote: String,
    val sourceReference: String,
    val updatedAt: String
)
```

---

## 3. WordEntryFtsEntity — Room FTS4 Virtual Table

```kotlin
// shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/feature/dictionary/data/local/entity/WordEntryFtsEntity.kt

@Fts4(
    contentEntity = WordEntryEntity::class,
    tokenizer = FtsOptions.TOKENIZER_UNICODE61
)
@Entity(tableName = "word_entries_fts")
data class WordEntryFtsEntity(
    @PrimaryKey @ColumnInfo(name = "rowid") val rowId: Int = 0,
    val muchikTerm: String,
    val spanishTranslation: String,
    val etymologyNote: String
)
```

**Notes**:
- Es una tabla FTS content-linked a `word_entries`. Cuando `WordEntryEntity` cambia,
  el índice FTS se actualiza automáticamente por Room.
- El tokenizador `unicode61` normaliza diacríticos — permite buscar "ae" y encontrar "æ".
- Solo indexa los 3 campos buscables (no categoría, emoji, sourceReference ni updatedAt).

---

## 4. WordCategory — Enum de categorías semánticas

```kotlin
// shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/feature/dictionary/domain/model/WordCategory.kt

enum class WordCategory(val displayName: String) {
    EDUCACION("Educación"),
    EXPRESIONES("Expresiones"),
    TIEMPO("Tiempo"),
    CONCEPTOS("Conceptos"),
    NATURALEZA("Naturaleza"),
    TOPONIMIA("Toponimia"),
    PRONOMBRES("Pronombres"),
    OBJETOS("Objetos"),
    PERSONAS("Personas"),
    ALL("Todas las Categorías");

    companion object {
        fun fromString(value: String): WordCategory =
            entries.firstOrNull { it.displayName == value } ?: CONCEPTOS
    }
}
```

---

## 5. WordEntryDto — DTO de red (Supabase REST)

```kotlin
// shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/feature/dictionary/data/remote/dto/WordEntryDto.kt

@Serializable
data class WordEntryDto(
    val id: String,
    @SerialName("muchik_term") val muchikTerm: String,
    @SerialName("spanish_translation") val spanishTranslation: String,
    val category: String,
    val emoji: String,
    @SerialName("etymology_note") val etymologyNote: String,
    @SerialName("source_reference") val sourceReference: String,
    @SerialName("updated_at") val updatedAt: String
)
```

---

## 6. Mapper — DTO ↔ Entity ↔ Domain

```kotlin
// shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/feature/dictionary/data/mapper/WordEntryMapper.kt

fun WordEntryDto.toEntity(): WordEntryEntity = WordEntryEntity(
    id = id,
    muchikTerm = muchikTerm,
    spanishTranslation = spanishTranslation,
    category = category,
    emoji = emoji,
    etymologyNote = etymologyNote,
    sourceReference = sourceReference,
    updatedAt = updatedAt
)

fun WordEntryEntity.toDomain(): WordEntry = WordEntry(
    id = id,
    muchikTerm = muchikTerm,
    spanishTranslation = spanishTranslation,
    category = category,
    emoji = emoji,
    etymologyNote = etymologyNote,
    sourceReference = sourceReference,
    updatedAt = updatedAt
)
```

---

## 7. Database — Room AppDatabase

```kotlin
// shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/core/database/AppDatabase.kt

@Database(
    entities = [WordEntryEntity::class, WordEntryFtsEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordEntryDao(): WordEntryDao
}
```

---

## 8. DAO — Consultas

```kotlin
// shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/feature/dictionary/data/local/dao/WordEntryDao.kt

@Dao
interface WordEntryDao {

    @Query("SELECT * FROM word_entries ORDER BY muchikTerm ASC")
    fun observeAll(): Flow<List<WordEntryEntity>>

    @Query("""
        SELECT we.* FROM word_entries we
        INNER JOIN word_entries_fts fts ON we.rowid = fts.rowid
        WHERE word_entries_fts MATCH :query
        ORDER BY we.muchikTerm ASC
    """)
    fun searchFts(query: String): Flow<List<WordEntryEntity>>

    @Query("SELECT * FROM word_entries WHERE muchikTerm LIKE '%' || :query || '%' OR spanishTranslation LIKE '%' || :query || '%' ORDER BY muchikTerm ASC")
    fun searchLike(query: String): Flow<List<WordEntryEntity>>

    @Query("SELECT * FROM word_entries WHERE category = :category ORDER BY muchikTerm ASC")
    fun observeByCategory(category: String): Flow<List<WordEntryEntity>>

    @Query("SELECT COUNT(*) FROM word_entries")
    suspend fun count(): Int

    @Query("SELECT category, COUNT(*) as count FROM word_entries GROUP BY category ORDER BY count DESC")
    suspend fun getCategoryCounts(): List<CategoryCount>

    @Upsert
    suspend fun upsertAll(entries: List<WordEntryEntity>)

    @Transaction
    suspend fun replaceAll(entries: List<WordEntryEntity>) {
        deleteAll()
        upsertAll(entries)
    }

    @Query("DELETE FROM word_entries")
    suspend fun deleteAll()
}

data class CategoryCount(val category: String, val count: Int)
```

---

## 9. Entidades estáticas (sin persistencia)

Las siguientes entidades son inmutables y se definen como datos en memoria:

### NumberEntry (Sistema Numérico)

```kotlin
data class NumberEntry(val value: Int, val muchikName: String)

// Datos completos en shared/commonMain/kotlin/.../feature/numbers/domain/model/Numbers.kt
val BASIC_NUMBERS = listOf(
    NumberEntry(1, "Onæc"),
    NumberEntry(2, "Atput"),
    NumberEntry(3, "Çoptör"),
    // ...
)
```

### NumeralClassifier (Clasificadores)

```kotlin
data class NumeralClassifier(
    val name: String,
    val description: String,
    val range: String
)
```

### GrammarBlock (Gramática)

```kotlin
data class GrammarBlock(
    val titleKey: String,
    val number: Int
)
// Contenido definido en strings.xml (compose resources) — no en modelo de datos
```

---

## 10. Estado de UI por pantalla

```kotlin
// Dictionary screen
data class DictionaryUiState(
    val isLoading: Boolean = false,
    val entries: List<WordEntry> = emptyList(),
    val query: String = "",
    val selectedCategory: WordCategory = WordCategory.ALL,
    val viewMode: DictionaryViewMode = DictionaryViewMode.CARDS,
    val totalCount: Int = 0,
    val categoryCounts: List<CategoryCount> = emptyList(),
    val errorMessage: String? = null
)

enum class DictionaryViewMode { CARDS, LIST }

// Quiz screen
data class QuizUiState(
    val question: WordEntry? = null,
    val options: List<WordEntry> = emptyList(),
    val selectedOption: WordEntry? = null,
    val isCorrect: Boolean? = null,
    val score: Int = 0
)

// Sync / first launch
data class SyncUiState(
    val isFirstLaunch: Boolean = false,
    val isSyncing: Boolean = false,
    val syncProgress: Float = 0f,
    val errorMessage: String? = null
)
```

---

## Relationships

```
word_entries (1) ←── FTS ───→ (1) word_entries_fts
                               [content-linked virtual table]

WordCategory ─── filters ───→ WordEntry list [derived, no FK]
```

---

## Schema Version History

| Version | Change | Migration |
|---------|--------|-----------|
| 1 | Schema inicial con word_entries + FTS | N/A — primera versión |
