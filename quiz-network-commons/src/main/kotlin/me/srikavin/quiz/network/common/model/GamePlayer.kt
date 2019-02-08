package me.srikavin.quiz.network.common.model

import me.srikavin.quiz.network.common.getString
import me.srikavin.quiz.network.common.getUUID
import me.srikavin.quiz.network.common.put
import java.nio.ByteBuffer
import java.util.*

data class GamePlayer(
        val id: UUID,
        val name: String,
        val score: Int,
        val avatar: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GamePlayer

        if (id != other.id) return false
        if (name != other.name) return false
        if (score != other.score) return false
        if (!avatar.contentEquals(other.avatar)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + score
        result = 31 * result + avatar.contentHashCode()
        return result
    }
}

fun GamePlayer.serialize(buffer: ByteBuffer) {
    buffer.put(id)
    buffer.put(name)
    buffer.putInt(score)
    buffer.putInt(avatar.size)
    buffer.put(avatar)
}

fun GamePlayer.countBytes(): Int {
    val array = name.toByteArray(Charsets.UTF_8)
    return 32 + 4 + 4 + array.size + 4 + avatar.size
}

fun deserializeGamePlayer(buffer: ByteBuffer) : GamePlayer {
    val id = buffer.getUUID()
    val name = buffer.getString()
    val score = buffer.int
    val avatarSize = buffer.int
    val avatar = ByteArray(avatarSize)
    buffer.get(avatar)
    return GamePlayer(id, name, score, avatar)
}

