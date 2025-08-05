package dev.deadzone.api.handler

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
            if (context.adminEnabled) {
                val session = context.authProvider.adminLogin()
                if (session != null) {
                    call.respond(HttpStatusCode.OK, mapOf("playerId" to session.playerId, "token" to session.token))
                } else {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("reason" to "unexpected error: admin account doesn't exist"))
                }
            } else {
                call.respond(HttpStatusCode.Forbidden, mapOf("reason" to "admin account not enabled"))
            }
            return@post
        }

        val usernameExist = context.authProvider.doesUserExist(username)
        if (usernameExist) {
            val loginSession = context.authProvider.login(username, password)
            val passwordRight = loginSession != null
            if (passwordRight) {
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("playerId" to loginSession.playerId, "token" to loginSession.token)
                )
            } else {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("reason" to "wrong password")
                )
            }
        } else {
            val session = context.authProvider.register(username, password)
            call.respond(
                HttpStatusCode.OK,
                mapOf("playerId" to session.playerId, "token" to session.token)
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
            if (context.adminEnabled) {
                call.respondText("granted")
            } else {
                call.respondText("reserved")
            }
            return@get
        }

        val exists = context.authProvider.doesUserExist(username)
        call.respondText(if (exists) "yes" else "no")
    }
}
