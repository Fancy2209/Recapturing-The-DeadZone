package dev.deadzone.context

import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.core.compound.CompoundRepositoryMongo
import dev.deadzone.core.compound.CompoundService
import dev.deadzone.core.items.InventoryRepositoryMongo
import dev.deadzone.core.items.InventoryService
import dev.deadzone.core.survivor.SurvivorRepositoryMongo
import dev.deadzone.core.survivor.SurvivorService
import dev.deadzone.data.collection.Inventory
import dev.deadzone.data.collection.NeighborHistory
import dev.deadzone.data.collection.PlayerAccount
import dev.deadzone.data.collection.PlayerObjects
import dev.deadzone.data.db.BigDB
import dev.deadzone.data.db.CollectionName
import dev.deadzone.socket.core.Connection
import io.ktor.util.date.getTimeMillis
import java.util.concurrent.ConcurrentHashMap

/**
 * Tracks each active player context, which is the player's in-memory data.
 */
class PlayerContextTracker {
    val players = ConcurrentHashMap<String, PlayerContext>()

    suspend fun createContext(
        playerId: String,
        connection: Connection,
        db: BigDB,
        useMongo: Boolean
    ) {
        val playerAccount =
            requireNotNull(db.loadPlayerAccount(playerId)) { "Missing PlayerAccount for playerid=$playerId" }

        players[playerId] = PlayerContext(
            playerId = playerId,
            connection = connection,
            onlineSince = getTimeMillis(),
            playerAccount = playerAccount,
            services = initializeServices(playerId, db, useMongo)
        )
    }

    private suspend fun initializeServices(
        playerId: String,
        db: BigDB,
        useMongo: Boolean
    ): PlayerServices {
        // if (useMongo)

        val plyObj =
            db.getCollection<MongoCollection<PlayerObjects>>(CollectionName.PLAYER_OBJECTS_COLLECTION)
        val neighbor =
            db.getCollection<MongoCollection<NeighborHistory>>(CollectionName.NEIGHBOR_HISTORY_COLLECTION)
        val inv =
            db.getCollection<MongoCollection<Inventory>>(CollectionName.INVENTORY_COLLECTION)

        val account =
            requireNotNull(db.loadPlayerAccount(playerId)) { "Weird, PlayerAccount for playerId=$playerId is null" }

        val survivor = SurvivorService(
            survivorLeaderId = account.playerMetadata.playerSrvId,
            survivorRepository = SurvivorRepositoryMongo(plyObj)
        )
        val inventory = InventoryService(inventoryRepository = InventoryRepositoryMongo())
        val compound = CompoundService(compoundRepository = CompoundRepositoryMongo(plyObj))

        return PlayerServices(
            survivor = survivor,
            compound = compound,
            inventory = inventory
        )
    }

    /**
     * Update the context of a player with a lambda function.
     *
     * The [update] method pass the current context and expects to return the updated context.
     */
    fun updateContext(playerId: String, update: (PlayerContext) -> PlayerContext) {
        val context = players.get(playerId) ?: return
        players[playerId] = update(context)
    }

    /**
     * Remove player to free-up memory.
     */
    fun removePlayer(playerId: String) {
        players.remove(playerId)
    }
}
