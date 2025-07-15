package dev.deadzone.api.handler

import dev.deadzone.api.message.db.LoadObjectsArgs
import dev.deadzone.api.message.db.LoadObjectsOutput
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
 * LoadObjects (API 85)
 *
 * Input: `[LoadObjectsArgs]`
 *
 * Output: `[LoadObjectsOutput]`
 */
@OptIn(ExperimentalSerializationApi::class)
suspend fun RoutingContext.loadObjects(db: BigDB) {
    val loadObjectsArgs = ProtoBuf.decodeFromByteArray<LoadObjectsArgs>(
        call.receiveChannel().toByteArray()
    )

    logApiMessage(loadObjectsArgs)

    val loadObjectsOutput = ProtoBuf.encodeToByteArray<LoadObjectsOutput>(
        LoadObjectsOutput.playerObjects()
    )

    logApiOutput(loadObjectsOutput)

    call.respondBytes(loadObjectsOutput.PIOFraming())
}
