package me.srikavin.quiz.network.common.message

import me.srikavin.quiz.network.common.getUUID
import me.srikavin.quiz.network.common.put
import java.nio.ByteBuffer
import java.util.*

data class AnswerQuestionMessage(val answerId: UUID) : MessageBase(ANSWER_QUESTION_PACKET_ID)

class AnswerQuestionSerializer : MessageSerializer<AnswerQuestionMessage> {
    override fun toBytes(t: AnswerQuestionMessage): ByteBuffer {
        val buffer = ByteBuffer.allocate(16)
        buffer.put(t.answerId)
        return buffer
    }

    override fun fromBytes(buffer: ByteBuffer): AnswerQuestionMessage {
        return AnswerQuestionMessage(buffer.getUUID())
    }
}