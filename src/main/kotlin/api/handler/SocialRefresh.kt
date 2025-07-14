package dev.deadzone.api.handler

import dev.deadzone.api.message.auth.SocialRefreshOutput
import dev.deadzone.core.BigDB
import dev.deadzone.module.PIOFraming
import dev.deadzone.module.logApiMessage
import dev.deadzone.module.logApiOutput
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
suspend fun RoutingContext.socialRefresh(db: BigDB) {
    val socialRefreshArgs = call.receiveChannel().toByteArray() // Actually no input is given

    logApiMessage(socialRefreshArgs.decodeToString())

    val socialRefreshOutput = ProtoBuf.encodeToByteArray<SocialRefreshOutput>(
        SocialRefreshOutput.dummy()
    )

    logApiOutput(socialRefreshOutput)

    call.respondBytes(socialRefreshOutput.PIOFraming())
}
