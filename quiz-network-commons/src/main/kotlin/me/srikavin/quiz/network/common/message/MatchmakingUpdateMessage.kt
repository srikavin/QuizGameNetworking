package me.srikavin.quiz.network.common.message

import me.srikavin.quiz.network.common.model.network.MatchmakerState

class MatchmakingUpdateMessage : MessageBase {
    constructor(state: MatchmakerState) : super(MATCHMAKER_UPDATE_PACKET_ID)
}
