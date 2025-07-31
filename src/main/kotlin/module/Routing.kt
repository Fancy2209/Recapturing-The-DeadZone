package dev.deadzone.module

import dev.deadzone.api.handler.*
import dev.deadzone.core.data.BigDB
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.io.File

fun Application.configureRouting(db: BigDB) {
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
        caseInsensitiveStaticResources("/game/data", "static")

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
                        when (msg) {
                            "close" -> break
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

        post("/api/{path}") {
            val path = call.parameters["path"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            when (path) {
                "13" -> authenticate(db)
                "601" -> socialRefresh(db)
                "27" -> createJoinRoom(db)
                "50" -> writeError(db)
                "85" -> loadObjects(db)
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
