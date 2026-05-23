package dev.tohure.muchik_dictionary.feature.dictionary

import dev.tohure.muchik_dictionary.feature.dictionary.data.repository.DictionaryRepositoryImpl
import dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase.SearchWordsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchWordsUseCaseTest {
    private val repository = DictionaryRepositoryImpl()
    private val useCase = SearchWordsUseCase(repository)

    @Test
    fun `empty query returns all entries`() =
        runTest {
            val results = useCase("").first()
            assertTrue(results.isNotEmpty(), "Debería retornar entradas con query vacío")
        }

    @Test
    fun `search by muchik term returns matching entries`() =
        runTest {
            val results = useCase("Apuk").first()
            assertTrue(results.any { it.muchikTerm.contains("Apuk", ignoreCase = true) })
        }

    @Test
    fun `search by spanish translation returns matching entries`() =
        runTest {
            val results = useCase("agua").first()
            assertTrue(results.any { it.spanishTranslation.contains("agua", ignoreCase = true) })
        }

    @Test
    fun `search with no match returns empty list`() =
        runTest {
            val results = useCase("xyznotexist12345").first()
            assertTrue(results.isEmpty())
        }

    @Test
    fun `search is case insensitive`() =
        runTest {
            val lower = useCase("sol").first()
            val upper = useCase("SOL").first()
            assertEquals(lower.map { it.id }.toSet(), upper.map { it.id }.toSet())
        }
}
