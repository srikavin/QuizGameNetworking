package me.srikavin.quiz.network.common.message

import me.srikavin.quiz.network.common.model.Quiz
import me.srikavin.quiz.network.common.model.QuizAnswer
import me.srikavin.quiz.network.common.model.QuizQuestion
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class StateUpdateMessageSerializerTest {

    @Test
    fun serializeQuizEmpty() {
        val questions = listOf<QuizQuestion>()
        val quiz = Quiz(UUID.randomUUID(), "testing quiz serialization", questions, "Quiz description")

        val serializer = StateUpdateMessageSerializer()
        val serialized = serializer.serializeQuiz(quiz)
        serialized.rewind()

        val deserialized = serializer.deserializeQuiz(serialized)

        assertEquals(quiz, deserialized)
        println(quiz)
        println(deserialized)
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

        val serializer = StateUpdateMessageSerializer()
        val serialized = serializer.serializeQuiz(quiz)
        serialized.rewind()

        val deserialized = serializer.deserializeQuiz(serialized)

        assertEquals(quiz, deserialized)
        println(quiz)
        println(deserialized)
    }
}