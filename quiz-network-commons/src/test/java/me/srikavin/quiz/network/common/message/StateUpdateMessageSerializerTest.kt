package me.srikavin.quiz.network.common.message

import me.srikavin.quiz.network.common.model.data.Quiz
import me.srikavin.quiz.network.common.model.data.QuizAnswer
import me.srikavin.quiz.network.common.model.data.QuizQuestion
import me.srikavin.quiz.network.common.model.game.GamePlayer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.util.*

internal class StateUpdateMessageSerializerTest {

    @Test
    fun serializeQuizEmpty() {
        val questions = listOf<QuizQuestion>()
        val quiz = Quiz(UUID.randomUUID(), "testing quiz serialization", questions, "Quiz description")

        val serializer = StateUpdateMessageSerializer()

        val serialized = ByteBuffer.allocate(serializer.count(quiz))

        serializer.serializeQuiz(serialized, quiz)

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
        val serialized = ByteBuffer.allocate(serializer.count(quiz))
        serializer.serializeQuiz(serialized, quiz)
        serialized.rewind()

        val deserialized = serializer.deserializeQuiz(serialized)

        assertEquals(quiz, deserialized)
        println(quiz)
        println(deserialized)
    }

    @Test
    fun messageSerialization() {
        val serializer = StateUpdateMessageSerializer()

        val answers = listOf(
                QuizAnswer(UUID.randomUUID(), "Answer A", true),
                QuizAnswer(UUID.randomUUID(), "Answer B", true),
                QuizAnswer(UUID.randomUUID(), "Answer C", false),
                QuizAnswer(UUID.randomUUID(), "Answer D", false)
        )
        val questions = listOf(QuizQuestion(UUID.randomUUID(), answers, "Sample Question"))
        val quiz = Quiz(UUID.randomUUID(), "testing quiz serialization", questions, "Quiz description")

        val player = GamePlayer(UUID.randomUUID(), "testing player", 1234, ByteArray(12))

        val message = StateUpdateMessage(GameState(quiz, Date(), listOf(player), 1))

        val serialized = serializer.toBytes(message)
        serialized.flip()

        val unserialized = serializer.fromBytes(serialized)

        assertEquals(message, unserialized)
    }
}