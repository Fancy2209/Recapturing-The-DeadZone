package dev.deadzone.module

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/", "static")

        post("/api/13") {
            call.respondText("asdfsd")
        }

        post("/api") {
            call.respondText("API 0")
        }
    }
}
