package dev.deadzone.module

import dev.deadzone.api.handler.*
import dev.deadzone.socket.ServerContext
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.io.File

fun Application.configureRouting(context: ServerContext) {
    routing {
        get("/") {
            val indexFile = File("static/index.html")
            if (indexFile.exists()) {
                call.respondFile(indexFile)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        staticFiles("/game", File("static/game/"))
        staticFiles("/assets", File("static/assets"))
        caseInsensitiveStaticResources("/game/data", File("static"))

        get("/debuglog") {
            val file = File("static/debuglog.html")
            if (file.exists()) {
                call.respondFile(file)
            } else {
                call.respond(HttpStatusCode.NotFound, "debuglog.html not found")
            }
        }

        webSocket("/debuglog") {
            val clientId = call.parameters["clientId"]
            if (clientId == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Missing clientId"))
                return@webSocket
            }

            Dependency.wsManager.addClient(clientId, this)

            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val msg = frame.readText()
                        try {
                            val wsMessage = Json.decodeFromString<WsMessage>(msg)
                            if (wsMessage.type == "close") break
                            Dependency.wsManager.handleMessage(this, wsMessage)
                        } catch (e: Exception) {
                            Logger.error { "Failed to parse WS message: $msg\n$e" }
                        }
                    }
                }
            } catch (e: Exception) {
                Logger.error { "Error in websocket for client $this: $e" }
            } finally {
                Dependency.wsManager.removeClient(clientId)
                Logger.info { "Client $this disconnected from websocket debug." }
            }
        }

        authRoute(context)

        get("/keepalive") {
            val token = call.parameters["token"] ?: return@get call.respond(HttpStatusCode.BadRequest, "missing token")
            if (context.sessionManager.refresh(token)) {
                return@get call.respond(HttpStatusCode.OK)
            } else {
                return@get call.respond(HttpStatusCode.Unauthorized, "Session expired, please login again")
            }
        }

        post("/api/{path}") {
            val path = call.parameters["path"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            when (path) {
                "13" -> authenticate(context)
                "601" -> socialRefresh(context)
                "27" -> createJoinRoom(context)
                "50" -> writeError(context)
                "85" -> loadObjects(context)
                else -> {
                    Logger.error(LogConfigAPIError) { "Unimplemented API route: $path" }
                    call.respond(HttpStatusCode.NotFound, "Unimplemented API: $path")
                }
            }
        }
    }
}

/**
 * Adds a PlayerIO framing prefix to the byte array.
 *
 * This is required by the PlayerIO API message convention, which expects each request and response
 * to be prefixed with two specific bytes: `0x00` and `0x01`.
 *
 * @receiver The original unframed [ByteArray] representing a protocol buffer message.
 * @return A new [ByteArray] with `0x00` and `0x01` prepended.
 */
fun ByteArray.pioFraming(): ByteArray {
    return byteArrayOf(0, 1) + this
}
