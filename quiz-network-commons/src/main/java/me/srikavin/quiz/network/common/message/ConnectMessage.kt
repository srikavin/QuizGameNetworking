package me.srikavin.quiz.network.common.message

class ConnectMessage(val rejoinToken: String? = null) : MessageBase(CONNECT_PACKET_ID)