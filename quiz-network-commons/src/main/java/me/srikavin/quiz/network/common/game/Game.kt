package me.srikavin.quiz.network.common.game

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.srikavin.quiz.network.common.message.MessageBase
import me.srikavin.quiz.network.common.message.game.AnswerQuestionMessage
import me.srikavin.quiz.network.common.message.game.GameState
import me.srikavin.quiz.network.common.message.game.StateUpdateMessage
import me.srikavin.quiz.network.common.model.data.Quiz
import me.srikavin.quiz.network.common.model.game.GameClient
import org.threeten.bp.Instant
import java.util.concurrent.atomic.AtomicInteger

const val TIME_PER_QUESTION = 30L
const val TIME_PER_QUESTION_MS = TIME_PER_QUESTION * 1000L
const val SCORE_TIME_MULTIPLIER_MS = 0.008

class Game(val quiz: Quiz, val players: List<NetworkGamePlayer>) {
    private val gamePlayers = players.map { it.player }.toCollection(mutableListOf())
    var state: GameState = GameState(quiz, Instant.now(), gamePlayers, 0)

    private var answersReceived = AtomicInteger(0)

    private fun sendAll(message: MessageBase) {
        players.forEach { player -> player.send(message) }
    }

    private fun sendState() {
        sendAll(StateUpdateMessage(state))
    }

    private fun sendState(player: NetworkGamePlayer) {
        player.send(StateUpdateMessage(state))
    }

    private fun startCoroutineTimer(questionNumber: Int) {
        GlobalScope.launch {
            delay(TIME_PER_QUESTION_MS)
            outOfTime(questionNumber)
        }
    }

    private fun nextQuestion() {
        state.copy(
                currentQuestion = state.currentQuestion + 1,
                timeLeft = getNextInstant()
        )
        sendState()
        startCoroutineTimer(state.currentQuestion)
    }

    private fun outOfTime(questionNumber: Int) {
        if (questionNumber != state.currentQuestion) {
            return
        }
        nextQuestion()
    }

    private fun getNextInstant(): Instant {
        return Instant.now().plusMillis(TIME_PER_QUESTION_MS)
    }

    private fun calculateScore(): Int {
        val remainingTime = state.timeLeft.toEpochMilli() - Instant.now().toEpochMilli()
        return (remainingTime * SCORE_TIME_MULTIPLIER_MS).toInt()
    }

    fun <T : MessageBase> start() {
        state = state.copy(
                timeLeft = getNextInstant()
        )
    }

    fun onAnswer(client: GameClient, message: AnswerQuestionMessage) {
        val player = players.find { it.id == client.backing.id }
        val answer = quiz.questions[state.currentQuestion].answers.find { answer -> answer.id == message.answerId }

        if (player == null || answer == null) {
            //Disregard message received that does not involve the current state of this game
            return
        }

        if (answer.isCorrect) {
            player.player.copy(
                    score = player.player.score + calculateScore()
            )
            sendState(player)
        }
        if (answersReceived.incrementAndGet() == players.size) {
            nextQuestion()
        }
    }
}
