package dev.deadzone.module

import dev.deadzone.core.data.BigDB
import dev.deadzone.socket.Server
import io.ktor.server.application.Application

fun Application.configureSocket(db: BigDB) {
    val server = Server(db = db)
    server.start()
    Runtime.getRuntime().addShutdownHook(Thread {
        server.stop()
    })
}
