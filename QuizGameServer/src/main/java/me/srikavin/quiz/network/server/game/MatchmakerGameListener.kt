package me.srikavin.quiz.network.server.game

import me.srikavin.quiz.network.common.MessageHandler
import me.srikavin.quiz.network.common.MessageRouter
import me.srikavin.quiz.network.common.game.Game
import me.srikavin.quiz.network.common.game.GameListener
import me.srikavin.quiz.network.common.game.NetworkGamePlayer
import me.srikavin.quiz.network.common.message.MATCHMAKER_START_PACKET_ID
import me.srikavin.quiz.network.common.message.MATCHMAKER_STOP_PACKET_ID
import me.srikavin.quiz.network.common.message.matchmaker.MatchMakingStartMessage
import me.srikavin.quiz.network.common.message.matchmaker.MatchMakingStopMessage
import me.srikavin.quiz.network.common.message.matchmaker.Matchmaker
import me.srikavin.quiz.network.common.model.game.GameClient

/**
 * Handles forwarding server packets to the appropriate room or matchmaker
 */
class MatchmakerGameListener(private val matchmaker: Matchmaker, messageRouter: MessageRouter) : GameListener(messageRouter) {
    private val clientRoomMap: MutableMap<NetworkGamePlayer, Game> = mutableMapOf()

    init {
        this.matchmaker.onGameCreate = { game ->
            game.players.forEach { player ->
                clientRoomMap[player] = game
            }
        }
        messageRouter.registerHandler(MATCHMAKER_START_PACKET_ID, object : MessageHandler<MatchMakingStartMessage> {
            override fun handle(client: GameClient, message: MatchMakingStartMessage) {
                matchmaker.addPlayer(message.quizId, client)
            }
        })
        messageRouter.registerHandler(MATCHMAKER_STOP_PACKET_ID, object : MessageHandler<MatchMakingStopMessage> {
            override fun handle(client: GameClient, message: MatchMakingStopMessage) {
                matchmaker.removePlayer(client)
            }
        })
    }
}