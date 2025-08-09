package dev.deadzone.socket.handler

import dev.deadzone.core.PlayerServiceLocator
import dev.deadzone.core.items.ItemFactory
import dev.deadzone.core.mission.LootService
import dev.deadzone.core.mission.model.LootParameter
import dev.deadzone.core.model.game.data.*
import dev.deadzone.core.survivor.SurvivorService
import dev.deadzone.module.Dependency
import dev.deadzone.module.LogConfigSocketError
import dev.deadzone.module.LogConfigSocketToClient
import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.handler.saveresponse.compound.BuildingCreateBuyResponse
import dev.deadzone.socket.handler.saveresponse.compound.BuildingCreateResponse
import dev.deadzone.socket.handler.saveresponse.compound.BuildingMoveResponse
import dev.deadzone.socket.handler.saveresponse.crate.CrateUnlockResponse
import dev.deadzone.socket.handler.saveresponse.mission.*
import dev.deadzone.socket.handler.saveresponse.survivor.PlayerCustomResponse
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler
import dev.deadzone.utils.PIOSerializer
import io.ktor.util.date.*
import java.util.*
import kotlin.random.Random

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

        // use pdoc to get player's data
        // to refactor: only query entire doc if needed, maybe create something like PlayerDataManager?
        val pid = connection.playerId!!
        val pdoc = requireNotNull(context.db.getUserDocByPlayerId(pid)) { "Weird, pdoc is null while in save handler" }
        val srvs = PlayerServiceLocator.get<SurvivorService>()
        val playerSrv = srvs.getSurvivorById(pdoc.playerSave.playerObjects.playerSurvivor)

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

            "ply_custom" -> {
                val ap = data?.get("ap") as? Map<*, *>

                val appearance = requireNotNull(
                    if (ap != null) {
                        HumanAppearance(
                            forceHair = ap["forceHair"] as? Boolean ?: false,
                            hideGear = ap["hideGear"] as? Boolean ?: false,
                            hairColor = ap["hairColor"] as? String ?: "black",
                            skinColor = ap["skinColor"] as? String,
                            hair = ap["hair"] as? String,
                            facialHair = ap["facialHair"] as? String,
                            clothing_upper = ap["upper"] as? String,
                            clothing_lower = ap["lower"] as? String,
                            accessories = (ap["accessories"] as? List<*>)?.mapNotNull { it as? String }
                        )
                    } else {
                        null
                    }, { "ply_custom response is null" })

                context.db.saveSurvivorAppearance(
                    playerId = pid,
                    srvId = requireNotNull(playerSrv?.id, { "Weird, playerSrv is null during saveSurvivorAppearance" }),
                    newAppearance = appearance
                )

                val responseJson = Dependency.json.encodeToString(PlayerCustomResponse())

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
                val lootParameter = LootParameter(
                    areaLevel = (data["areaLevel"] as Int),
                    playerLevel = playerSrv?.level ?: 1,
                    itemWeightOverrides = mapOf(),
                    specificItemBoost = mapOf(
                        "fuel-bottle" to 3.0,    // +300% find fuel chance (of the base chance)
                        "fuel-container" to 3.0,
                        "fuel" to 3.0,
                        "fuel-cans" to 3.0,
                    ),
                    itemTypeBoost = mapOf(
                        "junk" to 0.8 // +80% junk find chance
                    ),
                    itemQualityBoost = mapOf(
                        "blue" to 0.5 // +50% blue quality find chance
                    ),
                    baseWeight = 1.0,
                    fuelLimit = 50
                )
                val lootService = LootService(Dependency.gameResourceRegistry, sceneXML, lootParameter)
                val sceneXMLWithLoot = lootService.insertLoots()

                val zombies = listOf(
                    ZombieData.standardZombieWeakAttack(Random.nextInt()),
                    ZombieData.standardZombieWeakAttack(Random.nextInt()),
                    ZombieData.standardZombieWeakAttack(Random.nextInt()),
                    ZombieData.standardZombieWeakAttack(Random.nextInt()),
                    ZombieData.dogStandard(Random.nextInt()),
                    ZombieData.dogStandard(Random.nextInt()),
                    ZombieData.fatWalkerStrongAttack(Random.nextInt()),
                ).flatMap { it.toFlatList() }

                val timeSeconds = if (isCompoundZombieAttack == true) 30 else 240

                val responseJson = Dependency.json.encodeToString(
                    MissionStartResponse(
                        id = saveId ?: "",
                        time = timeSeconds,
                        assignmentType = "None", // 'None' because not a raid or arena. see AssignmentType
                        areaClass = (data["areaClass"] as String?) ?: "", // supposedly depend on the area
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
                // some of most important data
                val responseJson = Dependency.json.encodeToString(
                    MissionEndResponse(
                        automated = false,
                        xpEarned = 100,
                        xp = null,
                        returnTimer = null,
                        lockTimer = null,
                        loot = emptyList(),
                        // item id to quantity
                        itmCounters = emptyMap(),
                        injuries = null,
                        // the survivors that goes into the mission
                        survivors = emptyList(),
                        player = PlayerSurvivor(xp = 100, level = playerSrv?.level!!),
                        levelPts = 0,
                        // base64 encoded string
                        cooldown = null
                    )
                )

                // change resource with obtained loot...
                val currentResource = pdoc.playerSave.playerObjects.resources

                val resourceResponseJson = Dependency.json.encodeToString(currentResource)

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson, resourceResponseJson)))
            }

            "mis_zombies" -> {
                // usually requested during middle of mission
                // there could be 'rush' flag somewhere, which means we need to send runner zombies

                val zombies = listOf(
                    ZombieData.strongRunner(Random.nextInt()),
                    ZombieData.strongRunner(Random.nextInt()),
                    ZombieData.standardZombieWeakAttack(Random.nextInt()),
                    ZombieData.standardZombieWeakAttack(Random.nextInt()),
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
                Logger.debug(logFull = true) { data["stats"].toString() }
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

            "bld_create" -> {
                val response = BuildingCreateResponse(
                    // although client know user's resource,
                    // server may revalidate (in-case user did client-side hacking)
                    success = true,
                    items = emptyMap(),
                    timer = TimerData.fiveMinutesFromNow()
                )

                val responseJson = Dependency.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            "bld_create_buy" -> {
                val response = BuildingCreateBuyResponse(
                    success = true,
                    levelPts = 100
                )

                val responseJson = Dependency.json.encodeToString(response)
                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
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
