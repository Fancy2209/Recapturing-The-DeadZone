package dev.deadzone.api.handler

import dev.deadzone.api.message.auth.AuthenticateArgs
import dev.deadzone.api.message.auth.AuthenticateOutput
import dev.deadzone.core.data.AdminData
import dev.deadzone.module.Logger
import dev.deadzone.module.logInput
import dev.deadzone.module.logOutput
import dev.deadzone.module.pioFraming
import dev.deadzone.context.ServerContext
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

/**
 * Authenticate (API 13)
 *
 * Input: `AuthenticateArgs`
 *
 * Output: `AuthenticateOutput`
 */
@OptIn(ExperimentalSerializationApi::class)
suspend fun RoutingContext.authenticate(context: ServerContext) {
    val authenticateArgs = ProtoBuf.decodeFromByteArray<AuthenticateArgs>(
        call.receiveChannel().toByteArray()
    )

    logInput(authenticateArgs)

    val userToken = authenticateArgs
        .authenticationArguments
        .find { it.key == "userToken" }?.value

    if (userToken == null) {
        Logger.error { "Missing userToken in API 13 request" }
        call.respond(HttpStatusCode.BadRequest, "userToken is missing")
        return
    }

    val authenticateOutput = if (userToken == AdminData.TOKEN) {
        Logger.info { "auth by admin" }
        AuthenticateOutput.admin()
    } else {
        val isValidToken = context.sessionManager.verify(userToken)
        if (isValidToken) {
            AuthenticateOutput(
                token = userToken,
                userId = context.sessionManager.getPlayerId(userToken)!!,
                apiServerHosts = listOf("127.0.0.1:8080")
            )
        } else {
            call.respond(HttpStatusCode.Unauthorized, "token is invalid")
            null
        }
    } ?: return

    val encodedOutput = ProtoBuf.encodeToByteArray<AuthenticateOutput>(
        authenticateOutput
    )

    logOutput(encodedOutput)

    call.respondBytes(encodedOutput.pioFraming())
}
