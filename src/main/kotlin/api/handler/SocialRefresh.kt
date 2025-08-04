package dev.deadzone.api.handler

import dev.deadzone.api.message.auth.SocialRefreshOutput
import dev.deadzone.module.logInput
import dev.deadzone.module.logOutput
import dev.deadzone.module.pioFraming
import dev.deadzone.socket.ServerContext
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

/**
 * SocialRefresh (API 601)
 *
 * Input: `SocialRefreshArgs` (empty)
 *
 * Output: `SocialRefreshOutput`
 */
@OptIn(ExperimentalSerializationApi::class)
suspend fun RoutingContext.socialRefresh(context: ServerContext) {
    val socialRefreshArgs = call.receiveChannel().toByteArray() // Actually no input is given

    logInput(socialRefreshArgs.decodeToString())

    val socialRefreshOutput = ProtoBuf.encodeToByteArray<SocialRefreshOutput>(
        SocialRefreshOutput.dummy()
    )

    logOutput(socialRefreshOutput)

    call.respondBytes(socialRefreshOutput.pioFraming())
}
