package me.srikavin.quiz.network.common.model.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.util.*

internal class QuizKtTest {
    @Test
    fun serializeQuizEmpty() {
        val questions = listOf<QuizQuestion>()
        val quiz = Quiz(UUID.randomUUID(), "testing quiz serialization", questions, "Quiz description")

        val serialized = ByteBuffer.allocate(quiz.countBytes())

        quiz.serialize(serialized)

        assertEquals(quiz.countBytes(), serialized.position())

        serialized.rewind()

        val deserialized = deserializeQuiz(serialized)

        assertEquals(quiz, deserialized)
    }

    @Test
    fun serializeQuiz() {
        val answers = listOf(
                QuizAnswer(UUID.randomUUID(), "Answer A", true),
                QuizAnswer(UUID.randomUUID(), "Answer B", true),
                QuizAnswer(UUID.randomUUID(), "Answer C", false),
                QuizAnswer(UUID.randomUUID(), "Answer D", false)
        )
        val questions = listOf(QuizQuestion(UUID.randomUUID(), answers, "Sample Question"))
        val quiz = Quiz(UUID.randomUUID(), "testing quiz serialization", questions, "Quiz description")

        val serialized = ByteBuffer.allocate(quiz.countBytes())

        quiz.serialize(serialized)

        assertEquals(quiz.countBytes(), serialized.position())

        serialized.rewind()

        val deserialized = deserializeQuiz(serialized)

        assertEquals(quiz, deserialized)
    }
}