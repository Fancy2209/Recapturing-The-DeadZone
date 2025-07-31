package dev.deadzone.module

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebsocket() {
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        masking = true
    }
    Dependency.wsManager = WebsocketManager()
}

typealias ClientSessions = ConcurrentHashMap<String, DefaultWebSocketServerSession>

class WebsocketManager() {
    private val connectedDebugClients = ClientSessions()

    fun addClient(clientId: String, session: DefaultWebSocketServerSession) {
        connectedDebugClients.put(clientId, session)
    }

    fun removeClient(clientId: String): Boolean {
        return connectedDebugClients.remove(clientId) != null
    }

    fun getAllClients(): ClientSessions {
        return connectedDebugClients
    }

    fun getSessionFromId(clientId: String): DefaultWebSocketServerSession? {
        return connectedDebugClients[clientId]
    }
}
