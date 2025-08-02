package dev.deadzone.socket.handler

import dev.deadzone.core.mission.LootManager
import dev.deadzone.core.model.factory.ItemFactory
import dev.deadzone.core.model.game.data.*
import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.module.Dependency
import dev.deadzone.module.LogConfigSocketError
import dev.deadzone.module.LogConfigSocketToClient
import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.handler.saveresponse.compound.BuildingMoveResponse
import dev.deadzone.socket.handler.saveresponse.crate.CrateUnlockResponse
import dev.deadzone.socket.handler.saveresponse.crate.gachaPoolExample
import dev.deadzone.socket.handler.saveresponse.mission.GetZombieResponse
import dev.deadzone.socket.handler.saveresponse.mission.MissionEndResponse
import dev.deadzone.socket.handler.saveresponse.mission.MissionStartResponse
import dev.deadzone.socket.handler.saveresponse.mission.resolveAndLoadScene
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler
import io.ktor.util.date.*
import java.util.*

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

        // Note: the game typically send and expects JSON data for save message
        // encode JSON response to string before using PIO serialization

        when (type) {
            "get_offers" -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'get_offers' message [not implemented]" }
            }

            "chat_getContactsBlocks" -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'chat_getContactsBlocks' message [not implemented]" }
            }

            "crate_unlock" -> {
                val keyId = data["keyId"] as String?
                val crateId = (data["crateId"] ?: "") as String?

                val responseJson = Dependency.json.encodeToString(
                    CrateUnlockResponse(
                        success = true,
                        item = ItemFactory.getRandomItem(),
                        keyId = keyId,
                        crateId = crateId,
                    )
                )

                Logger.info(LogConfigSocketToClient) { "Opening crateId=$crateId with keyId=$keyId" }

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            "mis_start" -> {
                // IMPORTANT NOTE: the scene that involves human model is not working now (e.g., raid island human)
                // the same error is for survivor class if you fill SurvivorAppearance non-null value
                // The error was 'cyclic object' thing.
                val isCompoundZombieAttack = data["compound"]?.equals(true)
                val areaType = if (isCompoundZombieAttack == true) "compound" else data["areaType"] as String
                Logger.info(LogConfigSocketToClient) { "Going to scene with areaType=$areaType" }

                val sceneXML = resolveAndLoadScene(areaType)
                val sceneXMLWithLoot = LootManager().insertLoots(sceneXML)

                val zombies = listOf(
                    ZombieData.dogTank(101),
                    ZombieData.dogTank(111),
                    ZombieData.dogTank(112),
                    ZombieData.fatWalkerStrongAttack(101),
                    ZombieData.fatWalkerStrongAttack(102),
                    ZombieData.fatWalkerStrongAttack(103),
                    ZombieData.fatWalkerStrongAttack(104),
                    ZombieData.fatWalkerStrongAttack(105),
                    ZombieData.police20ZWeakAttack(113),
                    ZombieData.police20ZWeakAttack(114),
                    ZombieData.police20ZWeakAttack(115),
                    ZombieData.riotWalker37MediumAttack(116),
                    ZombieData.riotWalker37MediumAttack(117),
                    ZombieData.riotWalker37MediumAttack(118),
                ).flatMap { it.toFlatList() }

                val timeSeconds = if (isCompoundZombieAttack == true) 30 else 240

                val responseJson = Dependency.json.encodeToString(
                    MissionStartResponse(
                        id = saveId ?: "",
                        time = timeSeconds,
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
                Logger.info { "<----- Mission start flag received ----->" }
            }

            "mis_interacted" -> {
                Logger.info { "<----- First interaction received ----->" }
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
                // usually requested during middle of mission
                // there could be 'rush' flag somewhere, which means we need to send runner zombies

                val zombies = listOf(
                    ZombieData.strongRunner(101),
                    ZombieData.strongRunner(101),
                    ZombieData.strongRunner(101),
                    ZombieData.strongRunner(101),
                    ZombieData.strongRunner(101),
                    ZombieData.strongRunner(101),
                    ZombieData.strongRunner(101),
                    ZombieData.strongRunner(101),
                    ZombieData.strongRunner(101),
                    ZombieData.fatWalkerStrongAttack(101),
                    ZombieData.fatWalkerStrongAttack(102),
                    ZombieData.fatWalkerStrongAttack(103),
                    ZombieData.fatWalkerStrongAttack(104),
                    ZombieData.fatWalkerStrongAttack(105),
                ).flatMap { it.toFlatList() }

                val responseJson = Dependency.json.encodeToString(
                    GetZombieResponse(
                        max = false,
                        z = zombies
                    )
                )

                Logger.info(LogConfigSocketToClient) { "'mis_zombies' message (spawn zombie) request received" }

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            "stat_data" -> {
                val stats = data["stats"]
                Logger.warn(LogConfigSocketToClient) { "Received 'stat_data' message [not implemented] with stats: $stats" }
            }

            "clear_notes" -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'clear_notes' message [not implemented]" }
            }

            "give" -> {
                val type = data["type"] as? String ?: return

                Logger.info(LogConfigSocketToClient) { "Received 'give' command with type=$type | data=$data" }

                when (type) {
                    "schematic" -> {
                        // not tested
                        val schem = data["schem"] as? String ?: return
                        val item = SchematicItem(type = type, schem = schem, new = true)
                        val response = Dependency.json.encodeToString(item)
                        send(PIOSerializer.serialize(buildMsg(saveId, response)))
                    }

                    "crate" -> {
                        // not tested
                        val series = data["series"] as? Int ?: return
                        val repeat = (data["repeat"] as? Int) ?: 1
                        repeat(repeat) {
                            val item = CrateItem(type = type, series = series, new = true)
                            val response = Dependency.json.encodeToString(item)
                            send(PIOSerializer.serialize(buildMsg(saveId, response)))
                        }
                    }

                    "effect" -> {
                        Logger.warn(LogConfigSocketToClient) { "Received 'give' command of type effect [not implemented]" }
                    }

                    else -> {
                        // not tested with mod
                        val level = data["level"] as? Int ?: return
                        val qty = data["qty"] as? Int ?: 1
                        val mod1 = data["mod1"] as? String?
                        val mod2 = data["mod2"] as? String?
                        val item = Item(
                            id = UUID.randomUUID().toString(),
                            type = type,
                            level = level,
                            qty = qty.toUInt(),
                            mod1 = mod1,
                            mod2 = mod2,
                            new = true,
                        )
                        val response = Dependency.json.encodeToString(item)
                        send(PIOSerializer.serialize(buildMsg(saveId, response)))
                    }
                }
            }


            "giveRare" -> {
                val type = (data["type"] as String?) ?: return
                val level = (data["level"] as Int?) ?: return

                val item = Item(
                    id = UUID.randomUUID().toString(),
                    type = type,
                    level = level,
                    quality = 50,
                    new = true,
                )

                Logger.info(LogConfigSocketToClient) { "Received 'giveRare' command with type=$type | level=$level" }

                val response = Dependency.json.encodeToString(item)
                send(PIOSerializer.serialize(buildMsg(saveId, response)))
            }

            "giveUnique" -> {
                val type = (data["type"] as String?) ?: return
                val level = (data["level"] as Int?) ?: return

                val item = Item(
                    id = UUID.randomUUID().toString(),
                    type = type,
                    level = level,
                    quality = 51,
                    new = true,
                )

                Logger.info(LogConfigSocketToClient) { "Received 'giveUnique' command with type=$type | level=$level" }

                val response = Dependency.json.encodeToString(item)
                send(PIOSerializer.serialize(buildMsg(saveId, response)))
            }

            "bld_move" -> {
                val x = (data["tx"] as? Number)?.toInt() ?: 0
                val y = (data["ty"] as? Number)?.toInt() ?: 0
                val r = (data["rotation"] as? Number)?.toInt() ?: 0
                val buildingId = data["id"] // use this to refer the building

                Logger.debug(LogConfigSocketToClient) { "'bld_move' message for $saveId and $buildingId to tx=$x, ty=$y, rotation=$r" }

                val responseJson = Dependency.json.encodeToString(
                    BuildingMoveResponse(
                        success = true, x = x, y = y, r = r
                    )
                )

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            else -> {
                Logger.warn(LogConfigSocketError) { "Handled 's' network message but unhandled or unimplemented for save type: $type with data=$data" }
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
