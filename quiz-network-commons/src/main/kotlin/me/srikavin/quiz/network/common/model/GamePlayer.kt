package me.srikavin.quiz.network.common.model

import java.util.*

data class GamePlayer(
        val id: UUID,
        val name: String,
        val score: Int
)
