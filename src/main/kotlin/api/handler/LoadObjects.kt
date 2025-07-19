package dev.deadzone.api.handler

import dev.deadzone.api.message.db.BigDBObject
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

    val validUsers = setOf("user123", "userABC")
    val objs = mutableListOf<BigDBObject>()

    for (objId in loadObjectsArgs.objectIds) {
        val key = objId.keys.firstOrNull() ?: continue
        if (key !in validUsers) continue

        val obj: BigDBObject? = when (objId.table) {
            "PlayerObjects" -> LoadObjectsOutput.playerObjects()
            "NeighborHistory" -> LoadObjectsOutput.neighborHistory()
            "Inventory" -> LoadObjectsOutput.inventory()
            else -> {
                println("UNIMPLEMENTED table: ${objId.table}")
                null
            }
        }

        if (obj != null) objs.add(obj)
    }

    val loadObjectsOutput = ProtoBuf.encodeToByteArray(LoadObjectsOutput(objects = objs))

    logApiOutput(loadObjectsOutput)

    call.respondBytes(loadObjectsOutput.PIOFraming())
}
