package dev.deadzone

import dev.deadzone.core.auth.SessionManager
import dev.deadzone.auth.WebsiteAuthProvider
import dev.deadzone.core.data.GameResourceRegistry
import dev.deadzone.module.*
import dev.deadzone.socket.PlayerRegistry
import dev.deadzone.socket.ServerContext
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    EngineMain.main(args)
}

suspend fun Application.module() {
    configureWebsocket()
    Dependency.gameResourceRegistry = GameResourceRegistry(onResourceLoadComplete = {
        launch {
            Dependency.wsManager.onResourceLoadComplete()
        }
    })
    configureSerialization()
    val adminEnabled = environment.config.propertyOrNull("game.enableAdmin")?.getString()?.toBooleanStrictOrNull() ?: false
    val mongoUrl = environment.config.propertyOrNull("mongo.url")?.getString()!!
    configureDatabase(mongoUrl, adminEnabled)
    val sessionManager = SessionManager()
    val serverContext = ServerContext(
        db = Dependency.database,
        sessionManager = sessionManager,
        playerRegistry = PlayerRegistry(),
        authProvider = WebsiteAuthProvider(Dependency.database, sessionManager),
        adminEnabled = adminEnabled
    )
    configureRouting(context = serverContext)
    configureHTTP()
    configureLogging()
    Logger.level = LogLevel.DEBUG // use LogLevel.NOTHING to disable logging
    configureSocket(context = serverContext)
}
