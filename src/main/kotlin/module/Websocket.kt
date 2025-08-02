package dev.deadzone.module

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.websocket.Frame
import io.ktor.websocket.send
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
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

@Serializable
data class WsMessage(
    val type: String,
    val payload: JsonElement? = null,
)

typealias ClientSessions = ConcurrentHashMap<String, DefaultWebSocketServerSession>

class WebsocketManager() {
    private val connectedDebugClients = ClientSessions()
    private var resourceLoadCompleted: Boolean = false

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

    fun hasResourceLoadFinished() = resourceLoadCompleted

    suspend fun onResourceLoadComplete() {
        resourceLoadCompleted = true
        connectedDebugClients.values.forEach {
            it.send(Frame.Text(Json.encodeToString(WsMessage(type = "ready", payload = null))))
        }
    }

    suspend fun handleMessage(session: DefaultWebSocketServerSession, message: WsMessage) {
        when (message.type) {
            "isready" -> {
                val response = if (resourceLoadCompleted) "ready" else "notready"
                session.send(Frame.Text(Json.encodeToString(WsMessage(type = response, payload = null))))
            }
        }
    }
}
