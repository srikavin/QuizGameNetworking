package me.srikavin.quiz.network.common.model.network

import me.srikavin.quiz.network.common.model.game.Game

enum class MatchmakerStates(code: Byte) {
    SEARCHING(1),
    STOPPED(2),
    MATCH_FOUND(3),
}

data class MatchmakerState(val state: MatchmakerStates, val game: Game)