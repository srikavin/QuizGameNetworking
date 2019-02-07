package me.srikavin.quiz.network.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.srikavin.quiz.network.common.MessageRouter
import me.srikavin.quiz.network.common.message.MessageBase
import me.srikavin.quiz.network.common.model.RejoinToken
import me.srikavin.quiz.network.common.model.UserID
import me.srikavin.quiz.network.common.put
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.InetAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class NetworkClient(val remote: InetAddress, val packetRouter: MessageRouter) {
    lateinit var socket: Socket
    lateinit var networkScope: CoroutineScope
    lateinit var input: BufferedInputStream
    lateinit var output: BufferedOutputStream
    var rejoinToken: RejoinToken =
            RejoinToken(UUID.randomUUID())
    var userId: UserID =
            UserID(UUID.randomUUID())

    var connected: Boolean = false

    val queue = ConcurrentLinkedQueue<MessageBase>()

    fun start(networkScope: CoroutineScope, rejoinToken: RejoinToken? = null) {
        start(networkScope, rejoinToken?.token)
    }

    fun start(networkScope: CoroutineScope, rejoinToken: UUID? = null) {
        this.networkScope = networkScope

        this.networkScope.launch {
            messageHandler()
        }

//        rejoinToken =

        //Initialize connection
        networkScope.launch {
            socket = Socket(remote, 1200)
            socket.soTimeout = 15000
            input = socket.getInputStream().buffered()
            output = socket.getOutputStream().buffered()


            val welcome = ByteBuffer.allocate(20)

            //Length
            welcome.putInt(16)

            if (rejoinToken != null) {
                welcome.put(rejoinToken)
            }

            output.write(welcome.array())
            output.flush()

            val response = ByteArray(32)
            input.read(response)
            val responseBuffer = ByteBuffer.wrap(response)

            this@NetworkClient.rejoinToken = RejoinToken(responseBuffer)
            this@NetworkClient.userId = UserID(responseBuffer)

            println(this@NetworkClient)
            println(this@NetworkClient.rejoinToken)
            println(this@NetworkClient.userId)

            connected = true
        }
    }

    private suspend fun messageHandler() {
        var buffer: ByteBuffer = ByteBuffer.allocate(0)
        var inProgress = -1
        var total = 0
        while (true) {
            if (connected) {
                while (queue.isNotEmpty()) {
                    val base = queue.poll()
                    val serialized = packetRouter.serializeMessage(base)
                    output.write(serialized.array())
                }
                if (inProgress == total) {
                    //Hand-off to packet router
                    inProgress = -1
                    total = 0
                }

                if (input.available() > 0) {
                    if (inProgress != -1) {
                        if (inProgress < total) {
                            buffer.put(input.readNBytes(2))
                        }
                    } else {
                        total = ByteBuffer.wrap(input.readNBytes(4)).int
                        buffer = ByteBuffer.allocate(total)
                        inProgress = 0
                    }
                }
            }
            delay(200)
        }
    }

    fun close() {
        networkScope.coroutineContext.cancel()
        socket.close()
    }
}