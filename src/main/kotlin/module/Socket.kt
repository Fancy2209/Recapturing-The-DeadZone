package dev.deadzone.module

import dev.deadzone.socket.Server
import dev.deadzone.context.ServerContext

fun configureSocket(context: ServerContext) {
    val server = Server(context = context)
    server.start()
    Runtime.getRuntime().addShutdownHook(Thread {
        server.stop()
    })
}
