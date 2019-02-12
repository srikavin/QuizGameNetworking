package me.srikavin.quiz.network.server

import java.net.ServerSocket

fun main(args: Array<String>) {
    var port = 1200
    var dbName = "quizza"
    if (args.isNotEmpty()) {
        port = args[0].toInt()
    }
    if (args.size >= 2) {
        dbName = args[1]
    }

    val socket = ServerSocket(port)
    val server = Server(socket)
    server.start(dbName)
}