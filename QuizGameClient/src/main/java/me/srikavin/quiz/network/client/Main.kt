package me.srikavin.quiz.network.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.srikavin.quiz.network.common.MessageRouter
import java.net.InetAddress
import java.util.*

fun main() {
    val client = NetworkClient(InetAddress.getByName("localhost"), MessageRouter())
    client.start(CoroutineScope(Dispatchers.IO), null as UUID?)

    runBlocking {
        delay(100000)
    }
}