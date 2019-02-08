package me.srikavin.quiz.network.common.message

import me.srikavin.quiz.network.common.getString
import me.srikavin.quiz.network.common.getUUID
import me.srikavin.quiz.network.common.model.GamePlayer
import me.srikavin.quiz.network.common.model.Quiz
import me.srikavin.quiz.network.common.model.QuizAnswer
import me.srikavin.quiz.network.common.model.QuizQuestion
import me.srikavin.quiz.network.common.put
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList

data class GameState(
        val quiz: Quiz,
        val timeLeft: Date,
        val player: GamePlayer,
        val currentQuestion: Int
)

class StateUpdateMessage(val state: GameState) : MessageBase(CONNECT_PACKET_ID)

class StateUpdateMessageSerializer : MessageSerializer<StateUpdateMessage> {
    override fun toBytes(t: StateUpdateMessage): ByteBuffer {
        return null!!
    }

    override fun fromBytes(bytes: ByteBuffer): StateUpdateMessage {
        return StateUpdateMessage(null!!)
    }

    fun deserializeQuiz(buffer: ByteBuffer): Quiz {
        val id = buffer.getUUID()
        val contents = buffer.getString()
        val title = buffer.getString()

        val questionsNumber = buffer.int

        val questions = ArrayList<QuizQuestion>(questionsNumber)

        repeat(questionsNumber) {
            questions.add(deserializeQuizQuestion(buffer))
        }

        return Quiz(id, title, questions, contents)
    }


    fun serializeQuiz(quiz: Quiz): ByteBuffer {
        val length = count(quiz)
        val buffer = ByteBuffer.allocate(length)

        buffer.put(quiz.id)
        buffer.put(quiz.contents)
        buffer.put(quiz.title)
        buffer.putInt(quiz.questions.size)

        for (e in quiz.questions) {
            serializeQuizQuestion(buffer, e)
        }
        return buffer
    }

    private fun deserializeQuizQuestion(buffer: ByteBuffer): QuizQuestion {
        val id = buffer.getUUID()
        val contents = buffer.getString()
        val answersSize = buffer.int

        val answers = ArrayList<QuizAnswer>(answersSize)

        repeat(answersSize) {
            answers.add(deserializeQuizAnswer(buffer))
        }
        return QuizQuestion(id, answers, contents)
    }

    private fun serializeQuizQuestion(buffer: ByteBuffer, quizQuestion: QuizQuestion) {
        buffer.put(quizQuestion.id)
        buffer.put(quizQuestion.contents)
        buffer.putInt(quizQuestion.answers.size)
        for (e in quizQuestion.answers) {
            serializeQuizAnswer(buffer, e)
        }
    }

    private fun deserializeQuizAnswer(buffer: ByteBuffer): QuizAnswer {
        val id = buffer.getUUID()
        val isCorrect = buffer.get() == 1.toByte()
        val contents = buffer.getString()

        return QuizAnswer(id, contents, isCorrect)
    }

    private fun serializeQuizAnswer(buffer: ByteBuffer, quizAnswer: QuizAnswer) {
        buffer.put(quizAnswer.id)
        buffer.put(if (quizAnswer.isCorrect) 1.toByte() else 0)
        buffer.put(quizAnswer.contents)
    }

    private fun count(quiz: Quiz): Int {
        val contentsArray = quiz.contents.toByteArray(Charsets.UTF_8)
        val titleArray = quiz.title.toByteArray(Charsets.UTF_8)
        var questionLength = 0
        for (e in quiz.questions) {
            questionLength += countQuizQuestion(e)
        }

        return 16 +
                4 + // Int for size of title
                titleArray.size + // The title string
                4 + // Int for size of contents
                contentsArray.size + // The contents
                4 + // The number of questions
                questionLength // The questions
    }

    private fun countQuizQuestion(quizQuestion: QuizQuestion): Int {
        val contentsArray = quizQuestion.contents.toByteArray(Charsets.UTF_8)
        var answerLength = 0
        for (e in quizQuestion.answers) {
            answerLength += countQuizAnswer(e)
        }

        return 16 + // UUID
                4 + // Int for size of contents
                contentsArray.size + // The length of contents
                4 + // The number of answers
                answerLength // The answers
    }

    private fun countQuizAnswer(quizAnswer: QuizAnswer): Int {
        val contentsArray = quizAnswer.contents.toByteArray(Charsets.UTF_8)
        return 16 +  // UUID representing question
                11 + // Bit for if the value is correct
                4 +  // Int for size of contents
                contentsArray.size // Size of contents

    }
}