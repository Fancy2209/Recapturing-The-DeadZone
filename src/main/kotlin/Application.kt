package dev.deadzone

import dev.deadzone.module.*
import dev.deadzone.socket.Server
import io.ktor.server.application.*

fun main(args: Array<String>) {
    Server().start()
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDatabases()
    configureHTTP()
    configureRouting()
    configureSerialization()
    configureLogging()
}
