package dev.deadzone.api.handler

import dev.deadzone.api.message.auth.AuthenticateArgs
import dev.deadzone.api.message.auth.AuthenticateOutput
import dev.deadzone.module.logInput
import dev.deadzone.module.logOutput
import dev.deadzone.module.pioFraming
import dev.deadzone.socket.ServerContext
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

    val authenticateOutput = ProtoBuf.encodeToByteArray<AuthenticateOutput>(
        AuthenticateOutput.dummy()
    )

    logOutput(authenticateOutput)

    call.respondBytes(authenticateOutput.pioFraming())
}
