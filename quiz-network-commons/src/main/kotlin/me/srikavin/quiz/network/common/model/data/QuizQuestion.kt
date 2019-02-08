package me.srikavin.quiz.network.common.model.data

import me.srikavin.quiz.network.common.getString
import me.srikavin.quiz.network.common.getUUID
import me.srikavin.quiz.network.common.put
import java.nio.ByteBuffer
import java.util.*

data class QuizQuestion(
        val id: UUID,
        val answers: List<QuizAnswer>,
        val contents: String
)

fun QuizQuestion.countBytes(): Int {
    val contentsArray = this.contents.toByteArray(Charsets.UTF_8)
    var answerLength = 0
    for (e in this.answers) {
        answerLength += e.countBytes()
    }

    return 16 + // UUID
            4 + // Int for size of contents
            contentsArray.size + // The length of contents
            4 + // The number of answers
            answerLength // The answers
}

fun QuizQuestion.serialize(buffer: ByteBuffer) {
    buffer.put(this.id)
    buffer.put(this.contents)
    buffer.putInt(this.answers.size)
    for (e in this.answers) {
        e.serialize(buffer)
    }
}

fun deserializeQuizQuestion(buffer: ByteBuffer): QuizQuestion {
    val id = buffer.getUUID()
    val contents = buffer.getString()
    val answersSize = buffer.int

    val answers = ArrayList<QuizAnswer>(answersSize)

    repeat(answersSize) {
        answers.add(deserializeQuizAnswer(buffer))
    }
    return QuizQuestion(id, answers, contents)
}
