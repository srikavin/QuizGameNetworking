package me.srikavin.quiz.network.common

import me.srikavin.quiz.network.common.message.MessageBase
import me.srikavin.quiz.network.common.message.MessageIdentifier
import me.srikavin.quiz.network.common.message.MessageSerializer
import me.srikavin.quiz.network.common.model.game.GameClient
import java.nio.ByteBuffer

interface MessageHandler<in T : MessageBase> {
    fun handle(message: T) {

    }
}

class MessageRouter {
    private val packetMap: MutableMap<MessageIdentifier, MessageSerializer<in MessageBase>> = HashMap()
    private val handlerMap: MutableMap<MessageIdentifier, Set<MessageHandler<MessageBase>>> =
        mutableMapOf<MessageIdentifier, Set<MessageHandler<MessageBase>>>().withDefault { HashSet() }

    @Synchronized
    fun handlePacket(client: GameClient, message: ByteBuffer) {
        val id = MessageIdentifier(message.get())
        val serializer = packetMap[id] ?: throw RuntimeException("Unknown packet id: $id")
        val packet = serializer.fromBytes(message.slice()) as MessageBase
        handlePacket(client, packet)
    }

    @Synchronized
    fun handlePacket(client: GameClient, message: MessageBase) {
        println(message.javaClass.simpleName)
        handlerMap[message.identifier].orEmpty().forEach {
            it.handle(message)
        }
    }

    @Synchronized
    fun serializeMessage(message: MessageBase): ByteBuffer {
        return packetMap.get(message.identifier)?.toBytes(message)
            ?: throw RuntimeException("Unrecognized packet: ${message.identifier}; $message, has it been registered?)")
    }

    @Synchronized
    fun registerPacket(type: MessageIdentifier, serializer: MessageSerializer<in MessageBase>) {
    }

    @Synchronized
    fun <T : MessageBase> registerHandler(type: MessageIdentifier, handler: MessageHandler<T>) {
        handlerMap.putIfAbsent(type, HashSet())
    }
}