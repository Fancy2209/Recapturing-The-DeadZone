package dev.deadzone.api.handler

import dev.deadzone.socket.ServerContext
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.authRoute(context: ServerContext) {
    post("/api/login") {

    }

    get("/api/userexist") {
        val username = call.parameters["username"]
        if (username == null || username.isBlank()) {
            call.respondText("no", status = HttpStatusCode.BadRequest)
            return@get
        }

        val exists = false
        call.respondText(if (exists) "yes" else "no")
    }
}
