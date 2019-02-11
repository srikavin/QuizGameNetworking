package me.srikavin.quiz.network.server.game

import me.srikavin.quiz.network.common.game.Game
import me.srikavin.quiz.network.common.game.NetworkGamePlayer
import me.srikavin.quiz.network.common.message.matchmaker.Matchmaker
import me.srikavin.quiz.network.common.model.data.ResourceId
import me.srikavin.quiz.network.common.model.game.GameClient
import me.srikavin.quiz.network.common.model.game.GamePlayer
import me.srikavin.quiz.network.server.model.QuizRepository


const val PLAYERS_PER_GAME = 2

class NetworkMatchmaker(val onCreateGame: (Game) -> Unit, private val quizRepository: QuizRepository) : Matchmaker {
    override var onGameCreate: (Game) -> Unit = {}
    private val map: MutableMap<ResourceId, MutableList<GameClient>> = mutableMapOf()

    override fun addPlayer(resourceId: ResourceId, client: GameClient) {
        if (!map.containsKey(resourceId)) {
            map[resourceId] = ArrayList()
        }
        map[resourceId]?.add(client)
        if (map[resourceId]?.size == PLAYERS_PER_GAME) {
            val networkGamePlayers = map[resourceId]
                    ?.map { p -> NetworkGamePlayer(p, GamePlayer(p.backing.id, "Testing", 0, ByteArray(0))) }!!
            onCreateGame(Game(quizRepository.getQuiz(resourceId)!!, networkGamePlayers))
            map.remove(resourceId)
        }
    }

    override fun removePlayer(client: GameClient) {
        val toRemove = map.filter { e -> e.value.contains(client) }.toList()
        toRemove.forEach { remove -> map.remove(remove.first) }
    }

}