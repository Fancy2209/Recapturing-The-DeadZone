package dev.deadzone.module

import dev.deadzone.core.data.BigDB
import dev.deadzone.socket.Server

fun configureSocket(db: BigDB) {
    val server = Server(db = db)
    server.start()
    Runtime.getRuntime().addShutdownHook(Thread {
        server.stop()
    })
}
