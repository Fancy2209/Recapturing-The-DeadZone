package dev.deadzone.api.handler

import dev.deadzone.api.message.utils.WriteErrorArgs
import dev.deadzone.api.message.utils.WriteErrorError
import dev.deadzone.core.data.BigDB
import dev.deadzone.module.Logger
import dev.deadzone.module.logAPIInput
import dev.deadzone.module.logAPIOutput
import dev.deadzone.module.pioFraming
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

    logAPIInput("\n" + writeErrorArgs)
    Logger.writeError(writeErrorArgs)
    if (writeErrorArgs.details.contains("Load Never Completed", ignoreCase = true) ||
        writeErrorArgs.details.contains("Resource not found", ignoreCase = true)
    ) {
        Logger.writeMissingAssets(writeErrorArgs.details)
    }

    val writeErrorError = ProtoBuf.encodeToByteArray<WriteErrorError>(
        WriteErrorError.dummy()
    )

    logAPIOutput(writeErrorError)

    call.respondBytes(writeErrorError.pioFraming())
}
