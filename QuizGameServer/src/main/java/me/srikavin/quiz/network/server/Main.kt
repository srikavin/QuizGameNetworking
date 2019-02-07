package me.srikavin.quiz.network.server

import java.net.ServerSocket

fun main() {
    val socket = ServerSocket(1200)
    val server = Server(socket)
    server.start()
}