package dev.deadzone.api.handler

import dev.deadzone.api.message.utils.WriteErrorArgs
import dev.deadzone.api.message.utils.WriteErrorError
import dev.deadzone.core.BigDB
import dev.deadzone.module.PIOFraming
import dev.deadzone.module.logApiMessage
import dev.deadzone.module.logApiOutput
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

/**
 * WriteError (API 50)
 *
 * Input: `WriteErrorArgs`
 *
 * Output: `WriteErrorError` (optional)
 */
@OptIn(ExperimentalSerializationApi::class)
suspend fun RoutingContext.writeError(db: BigDB) {
    val writeErrorArgs = ProtoBuf.decodeFromByteArray<WriteErrorArgs>(
        call.receiveChannel().toByteArray()
    )

    logApiMessage(writeErrorArgs)

    val writeErrorError = ProtoBuf.encodeToByteArray<WriteErrorError>(
        WriteErrorError.dummy()
    )

    logApiOutput(writeErrorError)

    call.respondBytes(writeErrorError.PIOFraming())
}
