package dev.tohure.muchik_dictionary.feature.quiz

import dev.tohure.muchik_dictionary.feature.dictionary.data.repository.DictionaryRepositoryImpl
import dev.tohure.muchik_dictionary.feature.quiz.domain.usecase.GenerateQuizQuestionUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GenerateQuizQuestionUseCaseTest {

    private val repository = DictionaryRepositoryImpl()
    private val useCase = GenerateQuizQuestionUseCase(repository)

    @Test
    fun `generated question has exactly 4 options`() = runTest {
        val question = useCase()
        assertNotNull(question, "El corpus debe tener suficientes entradas para generar una pregunta")
        assertEquals(4, question.options.size)
    }

    @Test
    fun `correct answer is among the options`() = runTest {
        val question = useCase()
        assertNotNull(question)
        assertTrue(
            question.options.contains(question.correctAnswer),
            "La respuesta correcta debe estar entre las opciones"
        )
    }

    @Test
    fun `all options are distinct`() = runTest {
        val question = useCase()
        assertNotNull(question)
        assertEquals(
            question.options.toSet().size,
            question.options.size,
            "Las opciones no deben repetirse"
        )
    }

    @Test
    fun `muchik term and correct answer are not blank`() = runTest {
        val question = useCase()
        assertNotNull(question)
        assertTrue(question.muchikTerm.isNotBlank())
        assertTrue(question.correctAnswer.isNotBlank())
    }

    @Test
    fun `consecutive questions are different`() = runTest {
        val terms = (1..10).mapNotNull { useCase()?.muchikTerm }.toSet()
        assertTrue(terms.size > 1, "Las preguntas consecutivas deben variar")
    }
}
