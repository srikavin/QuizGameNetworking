package me.srikavin.quiz.network.server

import me.srikavin.quiz.network.common.message.MessageBase
import me.srikavin.quiz.network.common.model.game.BackingClient
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.net.Socket
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

data class NetworkClient(
    val id: UUID,
    val socket: Socket,
    val reader: BufferedInputStream,
    val writer: BufferedOutputStream,
    val buffer: ByteArrayOutputStream = ByteArrayOutputStream(),
    var inProgress: Int = -1,
    var total: Int = -1
) : BackingClient {
    val messageQueue: Queue<MessageBase> = ConcurrentLinkedQueue()
    val shouldKick = AtomicBoolean(false)
    var isBusy = AtomicBoolean(false)
    var isConnected = AtomicBoolean(true)

    override fun kick() {
        shouldKick.set(true)
    }

    override fun send(message: MessageBase) {
        messageQueue.offer(message)
    }

    override fun isConnected(): Boolean {
        return isConnected.get()
    }
}
