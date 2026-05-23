package dev.tohure.muchik_dictionary

import dev.tohure.muchik_dictionary.feature.dictionary.data.repository.DictionaryRepositoryImpl
import dev.tohure.muchik_dictionary.feature.quiz.domain.usecase.GenerateQuizQuestionUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// Tests que corren únicamente en el target JVM (Desktop). Complementan commonTest
// verificando que el código compartido funciona correctamente en el entorno JVM.
class SharedLogicDesktopTest {

    private val repository = DictionaryRepositoryImpl()

    @Test
    fun `corpus has more than 400 entries on JVM`() = runTest {
        val entries = repository.observeAll().first()
        assertTrue(entries.size > 400, "El corpus debe tener más de 400 entradas")
    }

    @Test
    fun `quiz question generation works on JVM`() = runTest {
        val useCase = GenerateQuizQuestionUseCase(repository)
        val question = useCase()
        assertNotNull(question, "Debe poder generar una pregunta de quiz en JVM")
        assertTrue(question.options.size == 4)
    }
}
