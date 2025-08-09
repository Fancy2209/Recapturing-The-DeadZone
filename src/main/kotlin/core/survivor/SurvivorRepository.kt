package dev.deadzone.core.survivor

import dev.deadzone.core.model.game.data.Survivor

interface SurvivorRepository {
    /**
     * Get survivors of [playerId].
     */
    suspend fun getSurvivorsOfPlayerId(playerId: String): List<Survivor>
}
