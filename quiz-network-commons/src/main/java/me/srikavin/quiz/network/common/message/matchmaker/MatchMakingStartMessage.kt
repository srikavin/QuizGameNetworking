package me.srikavin.quiz.network.common.message.matchmaker

import me.srikavin.quiz.network.common.message.MATCHMAKER_START_PACKET_ID
import me.srikavin.quiz.network.common.message.MessageBase
import me.srikavin.quiz.network.common.message.MessageSerializer
import me.srikavin.quiz.network.common.model.data.ResourceId
import me.srikavin.quiz.network.common.model.data.countBytes
import me.srikavin.quiz.network.common.model.data.getResourceId
import me.srikavin.quiz.network.common.model.data.put
import java.nio.ByteBuffer

class MatchMakingStartMessage(val quizId: ResourceId) : MessageBase(MATCHMAKER_START_PACKET_ID)

class MatchMakingStartMessageSerializer : MessageSerializer<MatchMakingStartMessage> {
    override fun toBytes(t: MatchMakingStartMessage): ByteBuffer {
        val buffer = ByteBuffer.allocate(t.quizId.countBytes())
        buffer.put(t.quizId)
        return buffer
    }

    override fun fromBytes(buffer: ByteBuffer): MatchMakingStartMessage {
        val resourceId = buffer.getResourceId()
        return MatchMakingStartMessage(resourceId)
    }

}