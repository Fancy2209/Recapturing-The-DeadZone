package dev.deadzone.socket.handler

import dev.deadzone.core.mission.insertLoots
import dev.deadzone.core.model.game.data.GameResources
import dev.deadzone.core.model.game.data.ZombieData
import dev.deadzone.core.model.game.data.toFlatList
import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.module.Dependency
import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.handler.saveresponse.compound.BuildingMoveResponse
import dev.deadzone.socket.handler.saveresponse.crate.CrateUnlockResponse
import dev.deadzone.socket.handler.saveresponse.crate.gachaExample
import dev.deadzone.socket.handler.saveresponse.mission.GetZombieResponse
import dev.deadzone.socket.handler.saveresponse.mission.MissionEndResponse
import dev.deadzone.socket.handler.saveresponse.mission.MissionStartResponse
import dev.deadzone.socket.handler.saveresponse.mission.resolveAndLoadScene
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler
import io.ktor.util.date.*

/**
 * Handle `save` message by:
 *
 * 1. Receive the `data`, `_type`, and `id` for the said message.
 * 2. Route the save into the corresponding handler based on `_type`.
 * 3. Handlers determine what to do based on the given `data`.
 * 4. Optionally, response back a message of type 'r' with the expected JSON payload.
 *
 */
class SaveHandler(private val context: ServerContext) : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.contains("s") or (message.type?.equals("s") == true)
    }

    override suspend fun handle(
        connection: Connection,
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        val body = message.getMap("s")
        val data = body?.get("data") as? Map<*, *>
        val type = data?.get("_type") as? String
        val saveId = body?.get("id") as? String

        Logger.socketPrint("Save message got: type:$type | id:$saveId")

        // Note: the game typically send and expects JSON data for save message
        // encode JSON response to string before using PIO serialization

        when (type) {
            "get_offers" -> {}
            "chat_getContactsBlocks" -> {}
            "crate_unlock" -> {
                val responseJson = Dependency.json.encodeToString(
                    CrateUnlockResponse(
                        success = true,
                        item = gachaExample(),
                        keyId = data["keyId"] as String?,
                        crateId = (data["crateId"] ?: "") as String?,
                    )
                )

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            "mis_start" -> {
                // IMPORTANT NOTE: the scene that involves human model is not working now (e.g., raid island human)
                // the same error is for survivor class if you fill SurvivorAppearance non-null value
                // The error was 'cyclic object' thing.
                val areaType = data["areaType"] as String
                Logger.socketPrint("Going to scene with areaType=$areaType")

                val sceneXML = resolveAndLoadScene(areaType)
                val sceneXMLWithLoot = insertLoots(sceneXML)

                val zombies = listOf(
                    ZombieData.fatWalkerStrongAttack(101),
                    ZombieData.fatWalkerStrongAttack(102),
                    ZombieData.fatWalkerStrongAttack(103),
                    ZombieData.fatWalkerStrongAttack(104),
                    ZombieData.fatWalkerStrongAttack(105),
                    ZombieData.fatWalkerStrongAttack(106),
                    ZombieData.fatWalkerStrongAttack(107),
                    ZombieData.fatWalkerStrongAttack(108),
                    ZombieData.fatWalkerStrongAttack(109),
                    ZombieData.police20ZWeakAttack(110),
                    ZombieData.riotWalker37MediumAttack(111)
                ).flatMap { it.toFlatList() }

                val responseJson = Dependency.json.encodeToString(
                    MissionStartResponse(
                        id = saveId ?: "",
                        time = 300,
                        assignmentType = "None", // 'None' because not a raid or arena. see AssignmentType
                        areaClass = "substreet", // supposedly depend on the area
                        automated = false, // this depends on request data
                        sceneXML = sceneXMLWithLoot,
                        z = zombies,
                        allianceAttackerEnlisting = false,
                        allianceAttackerLockout = false,
                        allianceAttackerAllianceId = null,
                        allianceAttackerAllianceTag = null,
                        allianceMatch = false,
                        allianceRound = 0,
                        allianceRoundActive = false,
                        allianceError = false,
                        allianceAttackerWinPoints = 0
                    )
                )

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            // mis_startFlag and mis_interacted do not expect a response
            "mis_startFlag" -> {
                Logger.socketPrint("<----- Mission start flag received ----->")
            }

            "mis_interacted" -> {
                Logger.socketPrint("<----- First interaction received ----->")
            }

            "mis_end" -> {
                val responseJson = Dependency.json.encodeToString(MissionEndResponse())
                val resourceResponseJson = Dependency.json.encodeToString(
                    GameResources(
                        cash = 102000,
                        wood = (10000..100000).random(),
                        metal = (10000..100000).random(),
                        cloth = (10000..100000).random(),
                        water = (10000..100000).random(),
                        food = (10000..100000).random(),
                        ammunition = (10000..100000).random()
                    )
                )

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson, resourceResponseJson)))
            }

            "mis_zombies" -> {
                val responseJson = Dependency.json.encodeToString(
                    GetZombieResponse(
                        max = false,
                        z = listOf(
                            ZombieData.fatWalkerStrongAttack(1001),
                            ZombieData.fatWalkerStrongAttack(1002),
                            ZombieData.fatWalkerStrongAttack(1003),
                            ZombieData.fatWalkerStrongAttack(1004),
                            ZombieData.fatWalkerStrongAttack(1005),
                            ZombieData.fatWalkerStrongAttack(1006),
                            ZombieData.fatWalkerStrongAttack(1007),
                            ZombieData.fatWalkerStrongAttack(1008),
                            ZombieData.fatWalkerStrongAttack(1009),
                        ).flatMap { it.toFlatList() }
                    ))

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            "stat_data" -> {
                val stats = data["stats"]
                Logger.socketPrint("Received stat_data with stats: $stats")
            }

            "clear_notes" -> {
                Logger.socketPrint("Received clear_notes")
            }

            "bld_move" -> {
                val x = (data["tx"] as? Number)?.toInt() ?: 0
                val y = (data["ty"] as? Number)?.toInt() ?: 0
                val r = (data["rotation"] as? Number)?.toInt() ?: 0
                val buildingId = data["id"] // use this to refer the building

                Logger.socketPrint("Building move for $saveId and $buildingId to $x,$y|r:$r")

                val responseJson = Dependency.json.encodeToString(
                    BuildingMoveResponse(
                        success = true, x = x, y = y, r = r
                    )
                )

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            else -> {
                Logger.unimplementedSocket("Handled 's' message but unhandled data type: $type from data=$data")
            }
        }
    }

    fun buildMsg(saveId: String?, vararg payloads: Any): List<Any> {
        return buildList {
            add("r")
            add(saveId ?: "m")
            add(getTimeMillis())
            addAll(payloads)
        }
    }
}
