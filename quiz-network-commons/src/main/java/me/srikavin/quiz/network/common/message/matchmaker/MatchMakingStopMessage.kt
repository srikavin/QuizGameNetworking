package me.srikavin.quiz.network.common.message.matchmaker

import me.srikavin.quiz.network.common.message.MATCHMAKER_START_PACKET_ID
import me.srikavin.quiz.network.common.message.MessageBase
import me.srikavin.quiz.network.common.message.MessageSerializer
import java.nio.ByteBuffer

class MatchMakingStopMessage : MessageBase(MATCHMAKER_START_PACKET_ID)

class MatchMakingStopMessageSerializer : MessageSerializer<MatchMakingStopMessage> {
    override fun toBytes(t: MatchMakingStopMessage): ByteBuffer {
        return ByteBuffer.allocate(0)
    }

    override fun fromBytes(buffer: ByteBuffer): MatchMakingStopMessage {
        return MatchMakingStopMessage()
    }

}