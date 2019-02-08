package me.srikavin.quiz.network.common.model.game

import me.srikavin.quiz.network.common.message.MessageBase


interface BackingClient {
    fun kick()
    fun send(message: MessageBase)
    fun isConnected() : Boolean
}

class GameClient(var backing: BackingClient)