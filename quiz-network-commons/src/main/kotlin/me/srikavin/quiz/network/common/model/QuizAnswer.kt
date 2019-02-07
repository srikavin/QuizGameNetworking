package me.srikavin.quiz.network.common.model

import java.util.*

data class QuizAnswer(
        val id: UUID,
        val contents: String,
        val isCorrect: Boolean
)
