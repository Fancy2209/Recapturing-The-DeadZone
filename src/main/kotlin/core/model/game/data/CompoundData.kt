package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.BuildingCollection
import dev.deadzone.core.model.game.data.EffectCollection
import dev.deadzone.core.model.game.data.GameResources
import dev.deadzone.core.model.game.data.Morale
import dev.deadzone.core.model.data.PlayerData
import dev.deadzone.core.model.game.data.SurvivorCollection
import dev.deadzone.core.model.game.data.TaskCollection

@Serializable
data class CompoundData(
    val player: PlayerData?,
    val buildings: BuildingCollection = BuildingCollection(),
    val resources: GameResources = GameResources(),
    val survivors: SurvivorCollection = SurvivorCollection(),
    val tasks: TaskCollection = TaskCollection(),
    val effects: EffectCollection = EffectCollection(),
    val globalEffects: EffectCollection = EffectCollection(),
    val morale: Morale = Morale(),
    val moraleFilter: List<String> = listOf("food", "water", "security", "comfort")
)
