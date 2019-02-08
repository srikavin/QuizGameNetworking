package me.srikavin.quiz.network.common.model.data

import me.srikavin.quiz.network.common.getString
import me.srikavin.quiz.network.common.getUUID
import me.srikavin.quiz.network.common.put
import java.nio.ByteBuffer
import java.util.*

data class QuizAnswer(
        val id: UUID,
        val contents: String,
        val isCorrect: Boolean
)

fun QuizAnswer.countBytes(): Int {
    val contentsArray = this.contents.toByteArray(Charsets.UTF_8)
    return 16 +  // UUID representing question
            1 + // Bit for if the value is correct
            4 +  // Int for size of contents
            contentsArray.size // Size of contents

}

fun QuizAnswer.serialize(buffer: ByteBuffer) {
    buffer.put(this.id)
    buffer.put(if (this.isCorrect) 1.toByte() else 0)
    buffer.put(this.contents)
}

fun deserializeQuizAnswer(buffer: ByteBuffer): QuizAnswer {
    val id = buffer.getUUID()
    val isCorrect = buffer.get() == 1.toByte()
    val contents = buffer.getString()

    return QuizAnswer(id, contents, isCorrect)
}
