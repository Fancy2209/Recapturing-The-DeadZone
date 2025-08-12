package dev.deadzone.core

import dev.deadzone.core.model.game.data.*
import io.ktor.util.date.*
import kotlin.math.ceil
import kotlin.time.Duration.Companion.milliseconds

/**
 * Lazily update player's data based on timer or lastLogin
 */
object LazyDataUpdater {
    fun depleteResources(lastLogin: Long, res: GameResources): GameResources {
        val minutesPassed = (getTimeMillis().milliseconds - lastLogin.milliseconds).inWholeMinutes
        val depletionRate = 0.05
        // depletion formula: each minutes deplete res by 0.05, an hour is 3

        val depleted = ceil(minutesPassed * depletionRate)

        return res.copy(
            food = res.food - (depleted).toInt(),
            water = res.water - (depleted).toInt()
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

            when (bld) {
                is Building -> when {
                    upgradeDone -> {
                        val level = bld.upgrade?.data?.get("level") ?: 1.0
                        bld.copy(level = level.toInt(), upgrade = null)
                    }
                    repairDone -> bld.copy(repair = null)
                    else -> bld
                }
                is JunkBuilding -> when {
                    upgradeDone -> {
                        val level = bld.upgrade?.data?.get("level") ?: 1.0
                        bld.copy(level = level.toInt(), upgrade = null)
                    }
                    repairDone -> bld.copy(repair = null)
                    else -> bld
                }
            }
        }
    }
}
