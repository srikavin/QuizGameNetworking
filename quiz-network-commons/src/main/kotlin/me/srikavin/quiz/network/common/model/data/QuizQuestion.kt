package me.srikavin.quiz.network.common.model.data

import java.util.*

data class QuizQuestion(
        val id: UUID,
        val answers: List<QuizAnswer>,
        val contents: String
)
