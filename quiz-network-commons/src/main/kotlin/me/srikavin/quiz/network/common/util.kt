package me.srikavin.quiz.network.common

import java.nio.ByteBuffer
import java.util.*

fun ByteBuffer.put(uuid: UUID) {
    this.putLong(uuid.mostSignificantBits)
    this.putLong(uuid.leastSignificantBits)
}
