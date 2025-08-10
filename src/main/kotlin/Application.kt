package dev.deadzone

import dev.deadzone.core.auth.SessionManager
import dev.deadzone.user.auth.WebsiteAuthProvider
import dev.deadzone.core.data.GameResourceRegistry
import dev.deadzone.module.*
import dev.deadzone.socket.OnlinePlayerRegistry
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


    val config = ServerConfig(
        adminEnabled = environment.config.propertyOrNull("game.enableAdmin")?.getString()?.toBooleanStrictOrNull() ?: false,
        useMongo = true,
        mongoUrl = environment.config.propertyOrNull("mongo.url")?.getString() ?: "",
        isProd = developmentMode,
    )

    configureDatabase(config.mongoUrl, config.adminEnabled)
    val sessionManager = SessionManager()
    val serverContext = ServerContext(
        db = Dependency.database,
        sessionManager = sessionManager,
        onlinePlayerRegistry = OnlinePlayerRegistry(),
        authProvider = WebsiteAuthProvider(Dependency.database, sessionManager),
        config = config,
    )
    configureRouting(context = serverContext)
    configureHTTP()
    configureLogging()
    Logger.level = LogLevel.DEBUG // use LogLevel.NOTHING to disable logging
    configureSocket(context = serverContext)
}
