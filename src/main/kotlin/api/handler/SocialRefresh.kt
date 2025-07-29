package dev.deadzone.api.handler

import dev.deadzone.api.message.auth.SocialRefreshOutput
import dev.deadzone.core.data.BigDB
import dev.deadzone.module.logAPIInput
import dev.deadzone.module.logAPIOutput
import dev.deadzone.module.pioFraming
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

    logAPIInput(socialRefreshArgs.decodeToString())

    val socialRefreshOutput = ProtoBuf.encodeToByteArray<SocialRefreshOutput>(
        SocialRefreshOutput.dummy()
    )

    logAPIOutput(socialRefreshOutput)

    call.respondBytes(socialRefreshOutput.pioFraming())
}
