package dev.tohure.muchik_dictionary.core.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<WordEntryEntity>)

    @Query("SELECT * FROM word_entry ORDER BY muchikTerm ASC")
    fun observeAll(): Flow<List<WordEntryEntity>>

    @Query("SELECT * FROM word_entry WHERE category = :category ORDER BY muchikTerm ASC")
    fun observeByCategory(category: String): Flow<List<WordEntryEntity>>

    @Query("""SELECT word_entry.* FROM word_entry
        INNER JOIN word_entry_fts ON word_entry.rowid = word_entry_fts.rowid
        WHERE word_entry_fts MATCH :query
        ORDER BY word_entry.muchikTerm ASC""")
    fun searchFts(query: String): Flow<List<WordEntryEntity>>

    @Query("SELECT COUNT(*) FROM word_entry")
    suspend fun count(): Long

    @Query("SELECT category, COUNT(*) as cnt FROM word_entry GROUP BY category")
    suspend fun getCategoryCounts(): List<CategoryCount>
}

data class CategoryCount(val category: String, val cnt: Int)
