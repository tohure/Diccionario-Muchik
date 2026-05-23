package dev.tohure.muchik_dictionary

import dev.tohure.muchik_dictionary.feature.dictionary.data.repository.DictionaryRepositoryImpl
import dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase.SearchWordsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

// Tests que corren únicamente en el target iOS. Complementan commonTest verificando
// que la lógica compartida funciona correctamente en el runtime de Kotlin/Native.
class SharedLogicIOSTest {

    private val repository = DictionaryRepositoryImpl()

    @Test
    fun `corpus is accessible on iOS Kotlin-Native runtime`() = runTest {
        val entries = repository.observeAll().first()
        assertTrue(entries.isNotEmpty(), "El corpus debe estar disponible en iOS")
    }

    @Test
    fun `search works correctly on Kotlin-Native`() = runTest {
        val useCase = SearchWordsUseCase(repository)
        val results = useCase("agua").first()
        assertTrue(
            results.any { it.spanishTranslation.contains("agua", ignoreCase = true) },
            "La búsqueda de 'agua' debe retornar resultados en iOS"
        )
    }
}
