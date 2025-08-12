package dev.deadzone.socket.handler.save.compound.building

import dev.deadzone.context.GlobalContext
import dev.deadzone.context.ServerContext
import dev.deadzone.context.requirePlayerContext
import dev.deadzone.core.model.game.data.GameResourcesConstants_Constants
import dev.deadzone.core.model.game.data.TimerData
import dev.deadzone.socket.handler.buildMsg
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.handler.save.compound.building.response.BuildingCancelResponse
import dev.deadzone.socket.handler.save.compound.building.response.BuildingCollectResponse
import dev.deadzone.socket.handler.save.compound.building.response.BuildingCreateBuyResponse
import dev.deadzone.socket.handler.save.compound.building.response.BuildingCreateResponse
import dev.deadzone.socket.handler.save.compound.building.response.BuildingMoveResponse
import dev.deadzone.socket.handler.save.compound.building.response.BuildingRecycleResponse
import dev.deadzone.socket.handler.save.compound.building.response.BuildingRepairResponse
import dev.deadzone.socket.handler.save.compound.building.response.BuildingRepairSpeedUpResponse
import dev.deadzone.socket.handler.save.compound.building.response.BuildingSpeedUpResponse
import dev.deadzone.socket.handler.save.compound.building.response.BuildingUpgradeResponse
import dev.deadzone.socket.messaging.SaveDataMethod
import dev.deadzone.socket.protocol.PIOSerializer
import dev.deadzone.utils.LogConfigSocketToClient
import dev.deadzone.utils.Logger
import kotlin.time.Duration.Companion.seconds

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
            // read buildings.xml from GameDefinition for items/build duration
            SaveDataMethod.BUILDING_CREATE -> {
                val bldId = data["id"] ?: return
                val bldType = data["type"] ?: return
                val x = data["tx"] ?: return
                val y = data["ty"] ?: return
                val r = data["rotation"] ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_CREATE' message for $saveId and $bldId,$bldType to tx=$x, ty=$y, rotation=$r" }

                val svc = serverContext.requirePlayerContext(playerId).services

                val response = BuildingCreateResponse(
                    // although client know user's resource,
                    // server may revalidate (in-case user did client-side hacking)
                    success = true,
                    items = emptyMap(),
                    timer = TimerData.runForDuration(10.seconds)
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
                val bldId = data["id"] ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_UPGRADE' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services

                val response = BuildingUpgradeResponse(
                    success = true,
                    items = emptyMap(),
                    timer = TimerData.runForDuration(10.seconds)
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_RECYCLE -> {
                val bldId = data["id"] ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_RECYCLE' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services

                val response = BuildingRecycleResponse(
                    success = true,
                    items = emptyMap(),
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_COLLECT -> {
                val bldId = data["id"] ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_COLLECT' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services

                val response = BuildingCollectResponse(
                    success = true,
                    locked = false,
                    resource = GameResourcesConstants_Constants.WOOD.value,
                    collected = 100,
                    remainder = 4,
                    total = 100 - 4,
                    bonus = 0,
                    destroyed = false,
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_CANCEL -> {
                val bldId = data["id"] ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_CANCEL' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services

                val response = BuildingCancelResponse(
                    success = true,
                    items = emptyMap(),
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_SPEED_UP -> {
                val bldId = data["id"] ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_SPEED_UP' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services

                val response = BuildingSpeedUpResponse(
                    success = true,
                    error = "",
                    cost = 1
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_REPAIR -> {
                val bldId = data["id"] ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_REPAIR' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services

                val response = BuildingRepairResponse(
                    success = true,
                    items = emptyMap(),
                    timer = TimerData.runForDuration(10.seconds)
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_REPAIR_SPEED_UP -> {
                val bldId = data["id"] ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_REPAIR_SPEED_UP' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services

                val response = BuildingRepairSpeedUpResponse(
                    success = true,
                    error = "",
                    cost = 1
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_CREATE_BUY -> {
                Logger.debug(LogConfigSocketToClient) { "Received 'BUILDING_CREATE_BUY' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_UPGRADE_BUY -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_UPGRADE_BUY' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_REPAIR_BUY -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_REPAIR_BUY' message [not implemented]" }
            }

            SaveDataMethod.BUILDING_TRAP_EXPLODE -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'BUILDING_TRAP_EXPLODE' message [not implemented]" }
            }
        }
    }
}
