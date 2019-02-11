package me.srikavin.quiz.network.common.message.matchmaker

import me.srikavin.quiz.network.common.message.MATCHMAKER_UPDATE_PACKET_ID
import me.srikavin.quiz.network.common.message.MessageBase
import me.srikavin.quiz.network.common.message.MessageSerializer
import java.nio.ByteBuffer

enum class MatchmakerStates(val code: Byte) {
    SEARCHING(1),
    STOPPED(2),
    MATCH_FOUND(3);

    companion object {
        fun fromCode(code: Byte): MatchmakerStates {
            for (e in MatchmakerStates.values()) {
                if (e.code == code) {
                    return e
                }
            }
            throw RuntimeException("Unknown code received from client: $code")
        }
    }
}

data class MatchmakerStateUpdateMessage(val state: MatchmakerStates) : MessageBase(MATCHMAKER_UPDATE_PACKET_ID)

class MatchmakerUpdateMessageSerializer : MessageSerializer<MatchmakerStateUpdateMessage> {
    override fun toBytes(t: MatchmakerStateUpdateMessage): ByteBuffer {
        val buffer = ByteBuffer.allocate(1)
        buffer.put(t.state.code)
        return buffer
    }

    override fun fromBytes(buffer: ByteBuffer): MatchmakerStateUpdateMessage {
        val code = buffer.get()
        return MatchmakerStateUpdateMessage(MatchmakerStates.fromCode(code))
    }

}