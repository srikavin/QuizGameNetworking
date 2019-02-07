package me.srikavin.quiz.network.common.model

enum class MatchmakerStates(code: Byte) {
    SEARCHING(1),
    STOPPED(2),
    MATCH_FOUND(3),
}

data class MatchmakerState(val state: MatchmakerStates, val game: Game)