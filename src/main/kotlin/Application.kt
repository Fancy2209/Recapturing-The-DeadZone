package dev.deadzone

import dev.deadzone.module.*
import io.ktor.server.application.*
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    configureWebsocket()
    configureRouting(db = Dependency.database)
    Dependency.gameData = GameData(onResourceLoadComplete = {
        launch {
            Dependency.wsManager.onResourceLoadComplete()
        }
    })
    configureHTTP()
    configureLogging()
    Logger.level = LogLevel.DEBUG // use LogLevel.NOTHING to disable logging
    configureSerialization()
    configureSocket(db = Dependency.database)
}
