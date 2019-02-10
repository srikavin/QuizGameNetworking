package me.srikavin.quiz.network.common.model.data

import me.srikavin.quiz.network.common.getString
import me.srikavin.quiz.network.common.put
import java.nio.ByteBuffer
import java.util.*

interface QuizQuestion {
    val id: ResourceId
    val answers: List<QuizAnswer>
    val contents: String
}

data class NetworkQuizQuestion(
        override val id: ResourceId,
        override val answers: List<QuizAnswer>,
        override val contents: String
) : QuizQuestion

fun QuizQuestion.countBytes(): Int {
    val contentsArray = this.contents.toByteArray(Charsets.UTF_8)
    var answerLength = 0
    for (e in this.answers) {
        answerLength += e.countBytes()
    }

    return id.countBytes() + // UUID
            4 + // Int for size of description
            contentsArray.size + // The length of description
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
    val id = buffer.getResourceId()
    val contents = buffer.getString()
    val answersSize = buffer.int

    val answers = ArrayList<QuizAnswer>(answersSize)

    repeat(answersSize) {
        answers.add(deserializeQuizAnswer(buffer))
    }
    return NetworkQuizQuestion(id, answers, contents)
}
