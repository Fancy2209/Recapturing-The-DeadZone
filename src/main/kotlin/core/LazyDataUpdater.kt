package dev.deadzone.core

import dev.deadzone.core.model.game.data.*
import io.ktor.util.date.*
import kotlin.time.Duration.Companion.milliseconds

/**
 * Lazily update player's data based on timer or lastLogin
 */
object LazyDataUpdater {
    fun depleteResources(lastLogin: Long, res: GameResources): GameResources {
        val hoursPassed = lastLogin.milliseconds.inWholeHours
        val depletionRate = 3
        // depletion formula: each hours deplete res by 3

        return res.copy(
            food = res.food - (hoursPassed * depletionRate).toInt(),
            water = res.water - (hoursPassed * depletionRate).toInt()
        )
    }

    fun updateBuildingTimers(buildings: List<BuildingLike>): List<BuildingLike> {
        val now = getTimeMillis().milliseconds

        return buildings.map { bld ->
            val upgradeDone = bld.upgrade?.let {
                now >= it.start.milliseconds + it.length.milliseconds
            } ?: false
            val repairDone = bld.repair?.let {
                now >= it.start.milliseconds + it.length.milliseconds
            } ?: false

            if (upgradeDone) {
                bld.copy(level = bld.level + 1, upgrade = null)
            } else if (repairDone) {
                bld.copy(repair = null)
            } else {
                bld
            }
        }
    }
}
