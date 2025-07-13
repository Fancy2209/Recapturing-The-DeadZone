package dev.deadzone.api.handler

import dev.deadzone.api.message.utils.WriteErrorArgs
import dev.deadzone.module.logApiMessage
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

/**
 * WriteError (API 50)
 *
 * Input: `WriteErrorArgs`
 *
 * Output: `WriteErrorError` (optional)
 */
@OptIn(ExperimentalSerializationApi::class)
suspend fun RoutingContext.writeError() {
    val writeErrorArgs = ProtoBuf.decodeFromByteArray<WriteErrorArgs>(
        call.receiveChannel().toByteArray()
    )

    logApiMessage(writeErrorArgs)
}
