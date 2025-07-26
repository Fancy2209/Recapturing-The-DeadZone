package dev.deadzone.socket.handler

import dev.deadzone.core.model.game.data.Zombie
import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.module.Dependency
import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.handler.save.compound.SaveBuildingResponse
import dev.deadzone.socket.handler.save.mission.GetZombieResponse
import dev.deadzone.socket.handler.save.mission.MissionStartResponse
import dev.deadzone.socket.handler.save.mission.loadSceneXML
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
        val data = body?.get("data") as? Map<String, Any?>
        val type = data?.get("_type") as? String
        val saveId = body?.get("id") as? String

        Logger.socketPrint("Save message got: type:$type | id:$saveId")

        // Note: the game typically send and expects JSON data for save message
        // encode JSON response to string before using PIO serialization

        when (type) {
            "get_offers" -> {}
            "chat_getContactsBlocks" -> {}
            "mis_start" -> {
                // this depends on the mission area
                // IMPORTANT NOTE: the scene that involves human model is not working now (e.g., raid island human)
                // the same error is for survivor class if you fill SurvivorAppereance non-null value
                // The error was 'cylic object' thing.
//                val sceneXMLString = loadSceneXML("street-small-1.xml.gz")
//                val sceneXMLString = loadSceneXML("exterior-cityblock-5.xml.gz")
//                val sceneXMLString = loadSceneXML("exterior-stadium-1-no-spawn.xml.gz")
                val sceneXMLString = loadSceneXML("interior-subway-large-3.xml.gz")

                val missionStartObjectResponse = MissionStartResponse(
                    id = saveId ?: "",
                    time = 200,
                    assignmentType = "None", // for simplicity. see AssignmentType
                    areaClass = "substreet",
                    automated = false,
                    rawSceneXML = loadSceneXML(sceneXMLString),
                    z = listOf(
                        Zombie.fatWalker(level = 10),
                        Zombie.fatWalker(level = 12),
                        Zombie.fatWalker(level = 12),
                        Zombie.fatWalker(level = 12),
                        Zombie.fatWalker(level = 12),
                        Zombie.fatWalker(level = 12),
                        Zombie.fatWalker(level = 12),
                        Zombie.fatWalker(level = 12),
                        Zombie.fatWalker(level = 12),
                        Zombie.fatWalker(level = 12),
                        Zombie.fatWalker(level = 12),
                    ),
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

                val responseJson = Dependency.json.encodeToString(missionStartObjectResponse)

                val msg = listOf(
                    "r",
                    saveId ?: "m",
                    getTimeMillis(),
                    responseJson
                )
                send(PIOSerializer.serialize(msg))
            }

            // mis_startFlag and mis_interacted do not expect a response
            "mis_startFlag" -> {
                Logger.socketPrint("===Mission start flag received===")
            }

            "mis_interacted" -> {
                Logger.socketPrint("===First interaction received===")
            }

            "mis_zombies" -> {
                val response = GetZombieResponse(max = true)
                val responseJson = Dependency.json.encodeToString(response)

                val msg = listOf(
                    "r",
                    saveId ?: "m",
                    getTimeMillis(),
                    responseJson
                )
                println(responseJson)
                send(PIOSerializer.serialize(msg))
            }

            "bld_move" -> {
                val x = (data["tx"] as? Number)?.toInt() ?: 0
                val y = (data["ty"] as? Number)?.toInt() ?: 0
                val r = (data["rotation"] as? Number)?.toInt() ?: 0
                val buildingId = data["id"] // use this to save in DB
                Logger.socketPrint("Building move for $saveId and $buildingId to $x,$y|r:$r")

                val response = SaveBuildingResponse(
                    success = true, x = x, y = y, r = r
                )
                val responseJson = Dependency.json.encodeToString(response)

                val msg = listOf(
                    "r",
                    saveId ?: "m",
                    getTimeMillis(),
                    responseJson
                )
                send(PIOSerializer.serialize(msg))
            }

            else -> {
                val msg = listOf(
                    "r",
                    saveId ?: "m",
                    getTimeMillis(),
                    "{}"
                )
                send(PIOSerializer.serialize(msg))
                Logger.unimplementedSocket("Handled 's' message but unhandled data type: $type from data=$data")
            }
        }
    }
}
