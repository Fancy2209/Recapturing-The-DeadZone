package dev.deadzone.socket.handler

import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.core.compound.CompoundRepositoryMongo
import dev.deadzone.core.compound.CompoundService
import dev.deadzone.core.items.InventoryRepositoryMongo
import dev.deadzone.core.items.InventoryService
import dev.deadzone.core.survivor.SurvivorRepositoryMongo
import dev.deadzone.core.survivor.SurvivorService
import dev.deadzone.data.collection.Inventory
import dev.deadzone.data.collection.NeighborHistory
import dev.deadzone.data.collection.PlayerObjects
import dev.deadzone.data.db.CollectionName
import dev.deadzone.module.LogSource
import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection
import dev.deadzone.ServerContext
import dev.deadzone.socket.TaskController
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler

/**
 * Handle `ic` message by:
 *
 * 1. Do the necessary setup in server.
 */
class InitCompleteHandler(
    private val context: ServerContext,
    private val taskController: TaskController
) :
    SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        // IC message is null, so only check for "ic" present
        return message.contains("ic")
    }

    override suspend fun handle(
        connection: Connection,
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        // Client part sends network INIT_COMPLETE message, with no handler attached
        // Likely only signal to server

        val pid = connection.playerId!!

        // When game init is completed, mark player as active
        context.onlinePlayerRegistry.markOnline(pid)

        // periodically send time update to client
        taskController.runTask("tu")
        taskController.addTaskCompletionCallback("tu") {
            Logger.info(LogSource.SOCKET) { "tu completed from ic" }
        }

        // register factory for game services
        if (context.config.useMongo) {
            // load all collections (second time after API 85)
            val plyObj =
                context.db.getCollection<MongoCollection<PlayerObjects>>(CollectionName.PLAYER_OBJECTS_COLLECTION)
            val neighbor =
                context.db.getCollection<MongoCollection<NeighborHistory>>(CollectionName.NEIGHBOR_HISTORY_COLLECTION)
            val inv =
                context.db.getCollection<MongoCollection<Inventory>>(CollectionName.INVENTORY_COLLECTION)

            val account =
                requireNotNull(context.db.loadPlayerAccount(pid)) { "Werid, PlayerAccount for playerId=$pid is null" }
            connection.updatePlayerAccount(account)

            connection.playerServiceLocator.registerFactory(SurvivorService::class) {
                val repo = SurvivorRepositoryMongo(plyObj)
                SurvivorService(survivorLeaderId = account.playerMetadata.playerSrvId, repo)
            }

            connection.playerServiceLocator.registerFactory(InventoryService::class) {
                val repo = InventoryRepositoryMongo()
                InventoryService(repo)
            }

            connection.playerServiceLocator.registerFactory(CompoundService::class) {
                val repo = CompoundRepositoryMongo(plyObj)
                CompoundService(repo)
            }
        } else {
            // use other db implementation...
        }
    }
}
