package dev.deadzone

import dev.deadzone.core.auth.SessionManager
import dev.deadzone.module.*
import dev.deadzone.socket.PlayerRegistry
import dev.deadzone.socket.ServerContext
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    val serverContext = ServerContext(
        db = Dependency.database,
        sessionManager = SessionManager(),
        playerRegistry = PlayerRegistry(),
    )
    configureWebsocket()
    configureRouting(context = serverContext)
    Dependency.gameData = GameData(onResourceLoadComplete = {
        launch {
            Dependency.wsManager.onResourceLoadComplete()
        }
    })
    configureHTTP()
    configureLogging()
    Logger.level = LogLevel.DEBUG // use LogLevel.NOTHING to disable logging
    configureSerialization()
    configureSocket(context = serverContext)
}
