package dev.deadzone.socket.handler.save.compound.building

import dev.deadzone.context.GlobalContext
import dev.deadzone.context.ServerContext
import dev.deadzone.context.requirePlayerContext
import dev.deadzone.core.model.game.data.*
import dev.deadzone.socket.handler.buildMsg
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.handler.save.compound.building.response.*
import dev.deadzone.socket.messaging.NetworkMessage
import dev.deadzone.socket.messaging.SaveDataMethod
import dev.deadzone.socket.protocol.PIOSerializer
import dev.deadzone.utils.LogConfigSocketToClient
import dev.deadzone.utils.Logger
import dev.deadzone.utils.toJsonElement
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Note about building operation:
 *
 * - There is no building or task complete signal. This also apply for other operations.
 * - Client keep tracks own timer and assumes that server also do it.
 * So we need to update the timer on upgrade/repair of Building.
 *
 * - We don't need to run a task that completes the upgrade, instead do it lazily.
 * That is when the client request data to server (such as during login)
 *
 * - We will check if timer start + length is lower than current time
 * If it is, remove the timer and apply change (i.e., increment level if upgrading)
 */
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
                val bldId = data["id"] as String? ?: return
                val bldType = data["type"] as String? ?: return
                val x = data["tx"] as Int? ?: return
                val y = data["ty"] as Int? ?: return
                val r = data["rotation"] as Int? ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_CREATE' message for $saveId and $bldId,$bldType to tx=$x, ty=$y, rotation=$r" }

                val buildDuration = 4.seconds

                val timer = TimerData.runForDuration(
                    duration = buildDuration,
                    data = mapOf("level" to 0, "type" to "upgrade").toJsonElement()
                )

                val svc = serverContext.requirePlayerContext(playerId).services
                svc.compound.createBuilding {
                    Building(
                        id = bldId,
                        name = null,
                        type = bldType,
                        level = 0, // always 0 because create
                        rotation = r,
                        tx = x,
                        ty = y,
                        destroyed = false,
                        resourceValue = 0.0,
                        upgrade = timer, // create can be thought as an upgrade to level 0
                        repair = null
                    )
                }

                val response = BuildingCreateResponse(
                    // although client know user's resource,
                    // server may revalidate (in-case user did client-side hacking)
                    success = true,
                    items = emptyMap(),
                    timer = timer
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))

                serverContext.taskDispatcher.runTask(NetworkMessage.BUILDING_COMPLETE) {
                    it.copy(
                        initialRunDelay = buildDuration + 3.seconds,
                        extra = mapOf("msg" to listOf(bldId, ))
                    )
                }
            }

            SaveDataMethod.BUILDING_MOVE -> {
                val x = (data["tx"] as? Number)?.toInt() ?: return
                val y = (data["ty"] as? Number)?.toInt() ?: return
                val r = (data["rotation"] as? Number)?.toInt() ?: return
                val buildingId = data["id"] as String? ?: return

                Logger.debug(LogConfigSocketToClient) { "'bld_move' message for $saveId and $buildingId to tx=$x, ty=$y, rotation=$r" }

                val svc = serverContext.requirePlayerContext(playerId).services
                svc.compound.updateBuilding(buildingId) {
                    it.copy(tx = x, ty = y, rotation = r)
                }

                val responseJson = GlobalContext.json.encodeToString(
                    BuildingMoveResponse(
                        success = true, x = x, y = y, r = r
                    )
                )

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_UPGRADE -> {
                val bldId = data["id"] as String? ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_UPGRADE' message for $saveId and $bldId" }

                lateinit var timer: TimerData
                val svc = serverContext.requirePlayerContext(playerId).services
                svc.compound.updateBuilding(bldId) { bld ->
                    timer = TimerData.runForDuration(
                        duration = 10.seconds,
                        data = mapOf("level" to (bld.level + 1), "type" to "upgrade").toJsonElement()
                    )
                    bld.copy(upgrade = timer)
                }

                val response = BuildingUpgradeResponse(
                    success = true,
                    items = emptyMap(),
                    timer = timer
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_RECYCLE -> {
                val bldId = data["id"] as String? ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_RECYCLE' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services
                svc.compound.deleteBuilding(bldId)

                val response = BuildingRecycleResponse(
                    success = true,
                    items = emptyMap(),
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_COLLECT -> {
                val bldId = data["id"] as String? ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_COLLECT' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services
                val collectResult = svc.compound.collectBuilding(bldId)
                val resType =
                    requireNotNull(collectResult.getNonEmptyResTypeOrNull()) { "Unexpected null on getNonEmptyResTypeOrNull during collect resource" }
                val resAmount =
                    requireNotNull(
                        collectResult.getNonEmptyResAmountOrNull()?.toDouble()
                    ) { "Unexpected null on getNonEmptyResAmountOrNull during collect resource" }

                val currentResource = svc.compound.getResources()
                val limit = 100.0 // this based on storage
                val expectedResource = currentResource.wood + resAmount
                val remainder = expectedResource - limit
                val total = max(limit, expectedResource)

                val response = BuildingCollectResponse(
                    success = true,
                    locked = false,
                    resource = resType,
                    collected = resAmount,
                    remainder = remainder, // - with current resource
                    total = total, // + with current resource
                    bonus = 0.0,
                    destroyed = false,
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_CANCEL -> {
                val bldId = data["id"] as String? ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_CANCEL' message for $saveId and $bldId" }

                val svc = serverContext.requirePlayerContext(playerId).services
                svc.compound.cancelBuilding(bldId)

                val response = BuildingCancelResponse(
                    success = true,
                    items = emptyMap(),
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_SPEED_UP -> {
                // TODO
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
                val bldId = data["id"] as String? ?: return

                Logger.debug(LogConfigSocketToClient) { "'BUILDING_REPAIR' message for $saveId and $bldId" }

                val timer = TimerData.runForDuration(10.seconds)

                val svc = serverContext.requirePlayerContext(playerId).services
                svc.compound.updateBuilding(bldId) { bld ->
                    val x = bld.copy(repair = timer)
                    Logger.debug("${bld.id} = ${bldId} = ${x.id}")
                    x
                }

                val response = BuildingRepairResponse(
                    success = true,
                    items = emptyMap(),
                    timer = timer
                )

                val responseJson = GlobalContext.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.BUILDING_REPAIR_SPEED_UP -> {
                // TODO
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
