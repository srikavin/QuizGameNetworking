package me.srikavin.quiz.network.common.message

import me.srikavin.quiz.network.common.model.GamePlayer
import me.srikavin.quiz.network.common.model.Quiz
import me.srikavin.quiz.network.common.model.QuizAnswer
import me.srikavin.quiz.network.common.model.QuizQuestion
import me.srikavin.quiz.network.common.put
import java.nio.ByteBuffer
import java.util.*

data class GameState(
        val quiz: Quiz,
        val timeLeft: Date,
        val player: GamePlayer
)

class StateUpdateMessage(val state: GameState) : MessageBase(CONNECT_PACKET_ID)

class StateUpdateMessageSerializer : MessageSerializer<StateUpdateMessage> {
    override fun toBytes(t: StateUpdateMessage): ByteBuffer {
    }

    override fun fromBytes(bytes: ByteBuffer): StateUpdateMessage {
    }

    private fun serializeQuiz(quiz: Quiz): ByteBuffer {
        var length = 0
        val quizAnswerBuffers = mutableListOf<ByteBuffer>()

    }

    private fun serializeQuizQuestion(quizQuestion: QuizQuestion): ByteBuffer {
        var answersLength = 0
        val quizAnswerBuffers = mutableListOf<ByteBuffer>()
        for (e in quizQuestion.answers) {
            val serializedAnswer = serializeQuizAnswer(e)
            quizAnswerBuffers.add(serializedAnswer)
            answersLength += serializedAnswer.capacity()
        }

        val buffer = ByteBuffer.allocate()
    }

    private fun serializeQuizAnswer(quizAnswer: QuizAnswer): ByteBuffer {
        val contentsArray = quizAnswer.contents.toByteArray(Charsets.UTF_8)
        val length = countQuizAnswer(quizAnswer)
        val buffer = ByteBuffer.allocate(length)
        buffer.put(if (quizAnswer.isCorrect) 1.toByte() else 0)
        buffer.put(quizAnswer.id)
        buffer.putInt(contentsArray.size)
        buffer.put(contentsArray)
        return buffer
    }

    private fun countQuizQuestion(quizAnswer: QuizAnswer): Int {

    }

    private fun countQuizAnswer(quizAnswer: QuizAnswer): Int {
        val contentsArray = quizAnswer.contents.toByteArray(Charsets.UTF_8)
        return 1 +  // Bit for if the value is correct
                32 + // UUID representing question
                4 +  // Int for size of contents
                contentsArray.size // Size of contents

    }
}