package dev.deadzone.api.handler

import dev.deadzone.core.data.DummyData
import dev.deadzone.socket.ServerContext
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoute(context: ServerContext) {
    post("/api/login") {
        val data = call.receive<Map<String, String>>()
        val username = data["username"]
        val password = data["password"]

        if (username == null || password == null) {
            call.respond(HttpStatusCode.BadRequest, mapOf("reason" to "Missing credentials"))
            return@post
        }

        if (username == "givemeadmin") {
            call.respond(
                HttpStatusCode.OK,
                mapOf("playerId" to DummyData.PLAYER_ID, "token" to DummyData.TOKEN)
            )
            return@post
        }

        val usernameExist = false
        if (usernameExist) {
            val passwordRight = true
            if (passwordRight) {
                val playerId = "pid"
                val token = "loggedin"
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("playerId" to playerId, "token" to token)
                )
            } else {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("reason" to "wrong password")
                )
            }
        } else {
            val newPlayerId = "pidnew"
            val token = "registered"
            call.respond(
                HttpStatusCode.OK,
                mapOf("playerId" to newPlayerId, "token" to token)
            )
        }
    }

    get("/api/userexist") {
        val username = call.parameters["username"]
        if (username == null || username.isBlank()) {
            call.respondText("no", status = HttpStatusCode.BadRequest)
            return@get
        }

        if (username == "givemeadmin") {
            call.respondText("yes")
            return@get
        }

        val exists = false
        call.respondText(if (exists) "yes" else "no")
    }
}
