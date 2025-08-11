package dev.deadzone.socket.handler.save.compound.building

import dev.deadzone.context.GlobalContext
import dev.deadzone.context.ServerContext
import dev.deadzone.core.model.game.data.TimerData
import dev.deadzone.socket.handler.buildMsg
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.handler.save.compound.response.BuildingCreateBuyResponse
import dev.deadzone.socket.handler.save.compound.response.BuildingCreateResponse
import dev.deadzone.socket.handler.save.compound.response.BuildingMoveResponse
import dev.deadzone.socket.messaging.SaveDataMethod
import dev.deadzone.socket.protocol.PIOSerializer
import dev.deadzone.utils.LogConfigSocketToClient
import dev.deadzone.utils.Logger

class BuildingSaveHandler : SaveSubHandler {
    override val supportedTypes: Set<String> = SaveDataMethod.COMPOUND_BUILDING_SAVES

    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any?>,
        playerId: String,
        send: suspend (ByteArray) -> Unit,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.BUILDING_CREATE -> {
                val response = BuildingCreateResponse(
                    // although client know user's resource,
                    // server may revalidate (in-case user did client-side hacking)
                    success = true,
                    items = emptyMap(),
                    timer = TimerData.fiveMinutesFromNow()
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_CREATE_BUY -> {
                val response = BuildingCreateBuyResponse(
                    success = true,
                    levelPts = 100
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_MOVE -> {
                val x = (data["tx"] as? Number)?.toInt() ?: 0
                val y = (data["ty"] as? Number)?.toInt() ?: 0
                val r = (data["rotation"] as? Number)?.toInt() ?: 0
                val buildingId = data["id"] // use this to refer the building

                Logger.debug(LogConfigSocketToClient) { "'bld_move' message for $saveId and $buildingId to tx=$x, ty=$y, rotation=$r" }

                val responseJson = GlobalContext.json.encodeToString(
                    BuildingMoveResponse(
                        success = true, x = x, y = y, r = r
                    )
                )

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_UPGRADE -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_UPGRADE' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_UPGRADE_BUY -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_UPGRADE_BUY' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_RECYCLE -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_RECYCLE' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_COLLECT -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_COLLECT' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_CANCEL -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_CANCEL' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_SPEED_UP -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_SPEED_UP' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_REPAIR -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_REPAIR' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_REPAIR_BUY -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_REPAIR_BUY' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_REPAIR_SPEED_UP -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_REPAIR_SPEED_UP' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_TRAP_EXPLODE -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_TRAP_EXPLODE' message [not implemented]" }
            }
        }
    }
}
