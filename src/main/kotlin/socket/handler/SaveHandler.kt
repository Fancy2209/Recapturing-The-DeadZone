package dev.deadzone.socket.handler

import dev.deadzone.context.GlobalContext
import dev.deadzone.context.ServerContext
import dev.deadzone.context.requirePlayerContext
import dev.deadzone.core.items.ItemFactory
import dev.deadzone.core.items.model.CrateItem
import dev.deadzone.core.items.model.Item
import dev.deadzone.core.items.model.SchematicItem
import dev.deadzone.core.mission.LootService
import dev.deadzone.core.mission.model.LootParameter
import dev.deadzone.core.model.data.PlayerFlags
import dev.deadzone.core.model.game.data.*
import dev.deadzone.socket.core.Connection
import dev.deadzone.socket.handler.save.chat.ChatSaveHandler
import dev.deadzone.socket.handler.save.compound.response.BuildingCreateBuyResponse
import dev.deadzone.socket.handler.save.compound.response.BuildingCreateResponse
import dev.deadzone.socket.handler.save.compound.response.BuildingMoveResponse
import dev.deadzone.socket.handler.save.crate.response.CrateUnlockResponse
import dev.deadzone.socket.handler.save.mission.response.GetZombieResponse
import dev.deadzone.socket.handler.save.mission.response.MissionEndResponse
import dev.deadzone.socket.handler.save.mission.response.MissionStartResponse
import dev.deadzone.socket.handler.save.mission.response.PlayerSurvivor
import dev.deadzone.socket.handler.save.mission.response.resolveAndLoadScene
import dev.deadzone.socket.handler.save.survivor.response.PlayerCustomResponse
import dev.deadzone.socket.messaging.CommandMessage
import dev.deadzone.socket.messaging.NetworkMessage
import dev.deadzone.socket.messaging.SaveDataMethod
import dev.deadzone.socket.messaging.SocketMessage
import dev.deadzone.socket.messaging.SocketMessageHandler
import dev.deadzone.socket.protocol.PIOSerializer
import dev.deadzone.utils.LogConfigSocketError
import dev.deadzone.utils.LogConfigSocketToClient
import dev.deadzone.utils.Logger
import io.ktor.util.date.*
import java.util.*
import kotlin.random.Random

/**
 * Handle `save` message by:
 *
 * 1. Receive the `data`, `_type`, and `id` (save id) for the said message.
 * 2. Route the save into the corresponding handler based on `_type`.
 * 3. Handlers determine what to do based on the given `data`.
 * 4. Optionally, response back a message of type 'r' with the expected JSON payload and the given save id.
 */
class SaveHandler(private val serverContext: ServerContext) : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.contains(NetworkMessage.SAVE) or (message.type?.equals(NetworkMessage.SAVE) == true)
    }

    override suspend fun handle(
        connection: Connection,
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        val body = message.getMap(NetworkMessage.SAVE)
        val data = body?.get("data") as? Map<*, *>
        val type = data?.get("_type") as? String
        val saveId = body?.get("id") as? String
        val pid = requireNotNull(connection.playerId) { "Missing playerId on save message for connection=$connection" }

        // Note: the game typically send and expects JSON data for save message
        // encode JSON response to string before using PIO serialization

        when (type) {
            in SaveDataMethod.COMPOUND_BUILDING_SAVES -> {}
            in SaveDataMethod.COMPOUND_TASK_SAVES -> {}
            in SaveDataMethod.COMPOUND_MISC_SAVES -> {}
            in SaveDataMethod.ITEM_SAVES -> {}
            in SaveDataMethod.MISSION_SAVES -> {}
            in SaveDataMethod.SURVIVOR_SAVES -> {}
            in SaveDataMethod.RAID_SAVES -> {}
            in SaveDataMethod.ARENA_SAVES -> {}
            in SaveDataMethod.QUEST_SAVES -> {}
            in SaveDataMethod.CRATE_SAVES -> {}
            in SaveDataMethod.ALLIANCE_SAVES -> {}
            in SaveDataMethod.CHAT_SAVES -> {}
            in SaveDataMethod.BOUNTY_SAVES -> {}
            in SaveDataMethod.PURCHASE_SAVES -> {}
            in SaveDataMethod.MISC_SAVES -> {}
        }

        when (type) {

            SaveDataMethod.GET_OFFERS -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'get_offers' message [not implemented]" }
            }

            SaveDataMethod.CHAT_GET_CONTACTS_AND_BLOCKS -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'chat_getContactsBlocks' message [not implemented]" }
            }

            SaveDataMethod.CRATE_UNLOCK -> {
                val keyId = data["keyId"] as String?
                val crateId = (data["crateId"] ?: "") as String?

                val responseJson = GlobalContext.json.encodeToString(
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

            SaveDataMethod.PLAYER_CUSTOM -> {
                val ap = data["ap"] as? Map<*, *> ?: return
                val title = data["name"] as? String ?: return
                val voice = data["v"] as? String ?: return
                val gender = data["g"] as? String ?: return
                val appearance = HumanAppearance.parse(ap)
                if (appearance == null) {
                    Logger.error(LogConfigSocketToClient) { "Failed to parse rawappearance=$ap" }
                    return
                }

                val bannedNicknames = listOf("dick")
                val nicknameNotAllowed = bannedNicknames.any { bannedWord ->
                    title.contains(bannedWord)
                }
                if (nicknameNotAllowed) {
                    val responseJson = GlobalContext.json.encodeToString(
                        PlayerCustomResponse(error = "Nickname not allowed")
                    )
                    send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
                    return
                }

                val svc = serverContext.requirePlayerContext(pid).services

                svc.playerObjectMetadata.updatePlayerFlags(
                    flags = PlayerFlags.create(nicknameVerified = true)
                )

                svc.playerObjectMetadata.updatePlayerNickname(nickname = title)

                svc.survivor.updateSurvivor(
                    srvId = svc.survivor.survivorLeaderId,
                    newSurvivor = svc.survivor.getSurvivorLeader().copy(
                        title = title,
                        firstName = title.split(" ").firstOrNull() ?: "",
                        lastName = title.split(" ").getOrNull(1) ?: "",
                        voice = voice,
                        gender = gender,
                        appearance = appearance
                    )
                )

                val responseJson = GlobalContext.json.encodeToString(PlayerCustomResponse())

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.MISSION_START -> {
                // IMPORTANT NOTE: the scene that involves human model is not working now (e.g., raid island human)
                // the same error is for survivor class if you fill SurvivorAppearance non-null value
                // The error was 'cyclic object' thing.
                val isCompoundZombieAttack = data["compound"]?.equals(true)
                val areaType = if (isCompoundZombieAttack == true) "compound" else data["areaType"] as String
                Logger.info(LogConfigSocketToClient) { "Going to scene with areaType=$areaType" }

                val svc = serverContext.requirePlayerContext(pid).services
                val leader = svc.survivor.getSurvivorLeader()

                val sceneXML = resolveAndLoadScene(areaType)
                val lootParameter = LootParameter(
                    areaLevel = (data["areaLevel"] as Int),
                    playerLevel = leader.level,
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
                val lootService = LootService(GlobalContext.gameDefinitions, sceneXML, lootParameter)
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

                val responseJson = GlobalContext.json.encodeToString(
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
            SaveDataMethod.MISSION_START_FLAG -> {
                Logger.info { "<----- Mission start flag received ----->" }
            }

            SaveDataMethod.MISSION_INTERACTION_FLAG -> {
                Logger.info { "<----- First interaction received ----->" }
            }

            SaveDataMethod.MISSION_END -> {
                val svc = serverContext.requirePlayerContext(pid).services
                val leader = svc.survivor.getSurvivorLeader()

                // some of most important data
                val responseJson = GlobalContext.json.encodeToString(
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
                        player = PlayerSurvivor(xp = 100, level = leader.level!!),
                        levelPts = 0,
                        // base64 encoded string
                        cooldown = null
                    )
                )

                // change resource with obtained loot...
                val currentResource = svc.compound.getResources()

                val resourceResponseJson = GlobalContext.json.encodeToString(currentResource)

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson, resourceResponseJson)))
            }

            SaveDataMethod.MISSION_ZOMBIES -> {
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

                val responseJson = GlobalContext.json.encodeToString(
                    GetZombieResponse(
                        max = false,
                        z = zombies
                    )
                )

                Logger.info(LogConfigSocketToClient) { "'mis_zombies' message (spawn zombie) request received" }

                send(PIOSerializer.serialize(buildMsg(saveId, responseJson)))
            }

            SaveDataMethod.STAT_DATA -> {
                val stats = data["stats"]
                Logger.debug(logFull = true) { data["stats"].toString() }
                Logger.warn(LogConfigSocketToClient) { "Received 'stat_data' message [not implemented] with stats: $stats" }
            }

            SaveDataMethod.CLEAR_NOTIFICATIONS -> {
                Logger.warn(LogConfigSocketToClient) { "Received 'clear_notes' message [not implemented]" }
            }

            CommandMessage.GIVE -> {
                val type = data["type"] as? String ?: return

                Logger.info(LogConfigSocketToClient) { "Received 'give' command with type=$type | data=$data" }

                when (type) {
                    "schematic" -> {
                        // not tested
                        val schem = data["schem"] as? String ?: return
                        val item = SchematicItem(type = type, schem = schem, new = true)
                        val response = GlobalContext.json.encodeToString(item)
                        send(PIOSerializer.serialize(buildMsg(saveId, response)))
                    }

                    "crate" -> {
                        // not tested
                        val series = data["series"] as? Int ?: return
                        val repeat = (data["repeat"] as? Int) ?: 1
                        repeat(repeat) {
                            val item = CrateItem(type = type, series = series, new = true)
                            val response = GlobalContext.json.encodeToString(item)
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
                        val response = GlobalContext.json.encodeToString(item)
                        send(PIOSerializer.serialize(buildMsg(saveId, response)))
                    }
                }
            }


            CommandMessage.GIVE_RARE -> {
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

                val response = GlobalContext.json.encodeToString(item)
                send(PIOSerializer.serialize(buildMsg(saveId, response)))
            }

            CommandMessage.GIVE_UNIQUE -> {
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

                val response = GlobalContext.json.encodeToString(item)
                send(PIOSerializer.serialize(buildMsg(saveId, response)))
            }

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
