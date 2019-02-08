package me.srikavin.quiz.network.common.model

import java.util.*

data class Quiz(
        val id: UUID,
        val title: String,
        val questions: List<QuizQuestion>,
        val contents: String
)
