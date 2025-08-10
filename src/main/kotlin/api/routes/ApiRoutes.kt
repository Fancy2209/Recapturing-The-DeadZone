package dev.deadzone.api.routes

import dev.deadzone.api.handler.authenticate
import dev.deadzone.api.handler.createJoinRoom
import dev.deadzone.api.handler.loadObjects
import dev.deadzone.api.handler.socialRefresh
import dev.deadzone.api.handler.writeError
import dev.deadzone.utils.LogConfigAPIError
import dev.deadzone.utils.Logger
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.apiRoutes() {
    post("/api/{path}") {
        val path = call.parameters["path"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val playerToken = if (path != "13" && path != "50") {
            call.request.queryParameters["playertoken"]
                ?: call.request.headers["playertoken"]
                ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing playertoken")
        } else {
            null
        }

        when (path) {
            "13" -> authenticate(context)
            "601" -> socialRefresh(context, playerToken!!)
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
