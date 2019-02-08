package me.srikavin.quiz.network.common.message

import me.srikavin.quiz.network.common.getString
import me.srikavin.quiz.network.common.getUUID
import me.srikavin.quiz.network.common.model.*
import me.srikavin.quiz.network.common.put
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList

data class GameState(
        val quiz: Quiz,
        val timeLeft: Date,
        val players: List<GamePlayer>,
        val currentQuestion: Int
)

data class StateUpdateMessage(val state: GameState) : MessageBase(CONNECT_PACKET_ID)

class StateUpdateMessageSerializer : MessageSerializer<StateUpdateMessage> {
    override fun toBytes(t: StateUpdateMessage): ByteBuffer {
        var playerLengths = 0

        for (player in t.state.players) {
            playerLengths += player.countBytes()
        }
        val length = 8 +               // Long for epoch
                count(t.state.quiz) +       // Quiz
                4 +                        // Current question
                4 +                        // Number of players
                playerLengths

        val buffer = ByteBuffer.allocate(length)

        buffer.putLong(t.state.timeLeft.time)
        serializeQuiz(buffer, t.state.quiz)
        buffer.putInt(t.state.currentQuestion)
        buffer.putInt(t.state.players.size)

        for (player in t.state.players) {
            player.serialize(buffer)
        }
        return buffer
    }

    override fun fromBytes(buffer: ByteBuffer): StateUpdateMessage {
        val timeEpoch = buffer.long
        val timeLeft = Date(timeEpoch)

        val quiz = deserializeQuiz(buffer)
        val currentQuestion = buffer.int
        val playerSize = buffer.int
        val players = ArrayList<GamePlayer>(playerSize)

        println("quiz = $quiz")

        repeat(playerSize) {
            players.add(deserializeGamePlayer(buffer))
        }

        return StateUpdateMessage(GameState(quiz, timeLeft, players, currentQuestion))
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


    fun serializeQuiz(buffer: ByteBuffer, quiz: Quiz) {
        buffer.put(quiz.id)
        buffer.put(quiz.contents)
        buffer.put(quiz.title)
        buffer.putInt(quiz.questions.size)

        for (e in quiz.questions) {
            serializeQuizQuestion(buffer, e)
        }
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

    fun count(quiz: Quiz): Int {
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