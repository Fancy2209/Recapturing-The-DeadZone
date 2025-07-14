package dev.deadzone

import dev.deadzone.module.*
import dev.deadzone.socket.Server
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDatabases()
    configureHTTP()
    configureRouting()
    configureSerialization()
    configureLogging()

    val server = Server()
    server.start()
    Runtime.getRuntime().addShutdownHook(Thread {
        server.stop()
    })
}
