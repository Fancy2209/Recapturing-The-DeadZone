package dev.deadzone.core.auth.model

import dev.deadzone.core.model.data.PlayerData
import dev.deadzone.core.model.game.data.Inventory
import dev.deadzone.core.model.network.RemotePlayerData
import kotlinx.serialization.Serializable

/**
 * Game data of a player which includes the essential PIO PlayerObjects and player login state.
 */
@Serializable
data class PlayerSave(
    val playerObjects: PlayerData,
    val inventory: Inventory,
    val neighborHistory: Map<String, RemotePlayerData>? = emptyMap(),
    val loginState: PlayerLoginState,
) {
    companion object {
        fun admin(): PlayerSave {
            return PlayerSave(
                playerObjects = PlayerData.dummy(),
                inventory = Inventory.dummy(),
                neighborHistory = emptyMap(),
                loginState = PlayerLoginState.admin()
            )
        }

        fun newgame(): PlayerSave {
            return PlayerSave(
                playerObjects = PlayerData.dummy(),
                inventory = Inventory.newgame(),
                neighborHistory = emptyMap(),
                loginState = PlayerLoginState.newgame()
            )
        }
    }
}
