package me.srikavin.quiz.network.common.message.matchmaker

import me.srikavin.quiz.network.common.message.CONNECT_PACKET_ID
import me.srikavin.quiz.network.common.message.MessageBase

class ConnectMessage(val rejoinToken: String? = null) : MessageBase(CONNECT_PACKET_ID)