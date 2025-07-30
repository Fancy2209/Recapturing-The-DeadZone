package dev.deadzone

import dev.deadzone.module.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    configureHTTP()
    configureRouting(db = Dependency.database)
    configureSerialization()
    configureLogging()
    configureWebsocket()
    configureSocket(db = Dependency.database)
}
