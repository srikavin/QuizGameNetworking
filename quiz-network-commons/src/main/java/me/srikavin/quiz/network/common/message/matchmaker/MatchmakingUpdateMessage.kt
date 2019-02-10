package me.srikavin.quiz.network.common.message.matchmaker

import me.srikavin.quiz.network.common.message.MATCHMAKER_UPDATE_PACKET_ID
import me.srikavin.quiz.network.common.message.MessageBase
import me.srikavin.quiz.network.common.model.network.MatchmakerState

class MatchmakingUpdateMessage : MessageBase {
    constructor(state: MatchmakerState) : super(MATCHMAKER_UPDATE_PACKET_ID)
}
