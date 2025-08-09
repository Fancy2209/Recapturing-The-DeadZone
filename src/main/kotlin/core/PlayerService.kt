package dev.deadzone.core

/**
 * Represents a player-scoped game service.
 *
 * This service manages data and domain logic related to a specific game domain for a particular player,
 * such as survivors, inventory, or loot. It is not responsible for low-level database operations,
 * nor should it handle player identification on each operations. Instead, it stores domain data and provides
 * operations to callers.
 *
 * Typically, the service initializes local data through the [init] method.
 * It receives a repository specific to the domain (e.g., [SurvivorRepository]) to delegates the
 * low-level database work.
 *
 * See examples: [SurvivorService]
 */
interface PlayerService {
    /**
     * Initializes the service for the specified [playerId].
     *
     * This method should be used to load or prepare all data related to the player
     * in this service's domain.
     */
    suspend fun init(playerId: String)
}
