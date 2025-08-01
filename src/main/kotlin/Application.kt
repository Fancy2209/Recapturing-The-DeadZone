package dev.deadzone

import dev.deadzone.module.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Dependency.gameData = GameData()
    configureDatabase()
    configureHTTP()
    configureLogging()
    Logger.level = LogLevel.DEBUG // use LogLevel.NOTHING to disable logging
    configureSerialization()
    configureWebsocket()
    configureRouting(db = Dependency.database)
    configureSocket(db = Dependency.database)
}
