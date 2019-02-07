package me.srikavin.quiz.network.common.model

import java.util.*

data class QuizQuestion(
        val id: UUID,
        val answers: List<QuizAnswer>,
        val contents: String
)
