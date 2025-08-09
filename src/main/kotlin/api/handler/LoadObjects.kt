package dev.deadzone.api.handler

import dev.deadzone.api.message.db.BigDBObject
import dev.deadzone.api.message.db.LoadObjectsArgs
import dev.deadzone.api.message.db.LoadObjectsOutput
import dev.deadzone.data.collection.NeighborHistory
import dev.deadzone.module.*
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
 * LoadObjects (API 85)
 *
 * Input: `[LoadObjectsArgs]`
 *
 * Output: `[LoadObjectsOutput]`
 */
@OptIn(ExperimentalSerializationApi::class)
suspend fun RoutingContext.loadObjects(context: ServerContext) {
    val loadObjectsArgs = ProtoBuf.decodeFromByteArray<LoadObjectsArgs>(
        call.receiveChannel().toByteArray()
    )

    logInput(loadObjectsArgs)

    val objs = mutableListOf<BigDBObject>()

    for (objId in loadObjectsArgs.objectIds) {
        val playerId = objId.keys.firstOrNull() ?: continue
        val udoc = context.db.getUserDocByPlayerId(playerId) ?: continue

        Logger.debug(src = LogSource.API) { "Found object for playerId: $playerId" }

        val obj: BigDBObject? = when (objId.table) {
            "PlayerObjects" -> LoadObjectsOutput.fromData(udoc.playerObjects)
            "NeighborHistory" -> LoadObjectsOutput.fromData(NeighborHistory(playerId = playerId, udoc.playerSave.neighborHistory))
            "Inventory" -> LoadObjectsOutput.fromData(udoc.playerSave.inventory)
            else -> {
                Logger.error(LogConfigAPIError) { "UNIMPLEMENTED table for ${objId.table}" }
                null
            }
        }

        if (obj != null) objs.add(obj)
    }

    val loadObjectsOutput = ProtoBuf.encodeToByteArray(LoadObjectsOutput(objects = objs))

//    logOutput(loadObjectsOutput)

    call.respondBytes(loadObjectsOutput.pioFraming())
}
