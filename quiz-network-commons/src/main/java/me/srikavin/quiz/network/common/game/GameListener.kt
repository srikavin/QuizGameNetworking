package me.srikavin.quiz.network.common.game

import me.srikavin.quiz.network.common.MessageHandler
import me.srikavin.quiz.network.common.MessageRouter
import me.srikavin.quiz.network.common.message.ANSWER_QUESTION_PACKET_ID
import me.srikavin.quiz.network.common.message.game.AnswerQuestionMessage
import me.srikavin.quiz.network.common.model.game.GameClient

class GameListener {
    constructor(messageRouter: MessageRouter) {
        messageRouter.registerHandler(ANSWER_QUESTION_PACKET_ID, object : MessageHandler<AnswerQuestionMessage> {
            override fun handle(client: GameClient, message: AnswerQuestionMessage) {

            }
        })
    }
}