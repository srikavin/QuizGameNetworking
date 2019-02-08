package me.srikavin.quiz.network.common.model.data

import me.srikavin.quiz.network.common.getString
import me.srikavin.quiz.network.common.getUUID
import me.srikavin.quiz.network.common.put
import java.nio.ByteBuffer
import java.util.*

data class Quiz(
        val id: UUID,
        val title: String,
        val questions: List<QuizQuestion>,
        val contents: String
)

fun Quiz.countBytes(): Int {
    val contentsArray = this.contents.toByteArray(Charsets.UTF_8)
    val titleArray: ByteArray = this.title.toByteArray(Charsets.UTF_8)
    var questionLength = 0
    for (e in this.questions) {
        questionLength += e.countBytes()
    }

    return 16 +
            4 + // Int for size of title
            titleArray.size + // The title string
            4 + // Int for size of contents
            contentsArray.size + // The contents
            4 + // The number of questions
            questionLength // The questions
}

fun Quiz.serialize(buffer: ByteBuffer) {
    buffer.put(this.id)
    buffer.put(this.contents)
    buffer.put(this.title)
    buffer.putInt(this.questions.size)

    for (e: QuizQuestion in this.questions) {
        e.serialize(buffer)
    }
}

fun deserializeQuiz(buffer: ByteBuffer): Quiz {
    val id = buffer.getUUID()
    val contents = buffer.getString()
    val title = buffer.getString()

    val questionsNumber = buffer.int

    val questions = ArrayList<QuizQuestion>(questionsNumber)

    repeat(questionsNumber) {
        questions.add(deserializeQuizQuestion(buffer))
    }

    return Quiz(id, title, questions, contents)
}
