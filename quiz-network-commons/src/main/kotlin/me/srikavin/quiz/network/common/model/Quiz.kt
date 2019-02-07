package me.srikavin.quiz.network.common.model

data class Quiz(
        val _id: Any,
        val title: String,
        val questions: List<QuizQuestion>,
        val contents: String
)
