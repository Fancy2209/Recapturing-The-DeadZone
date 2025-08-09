package dev.deadzone.core.auth.model

import dev.deadzone.core.data.PlayerLoginState
import dev.deadzone.data.collection.PlayerObjects
import dev.deadzone.data.collection.Inventory
import dev.deadzone.core.model.network.RemotePlayerData
import kotlinx.serialization.Serializable

/**
 * Game data of a player which includes the essential PIO PlayerObjects and player login state.
 */
@Serializable
data class PlayerSave(
    val playerObjects: PlayerObjects,
    val inventory: Inventory,
    val neighborHistory: Map<String, RemotePlayerData>? = emptyMap(),
    val loginState: PlayerLoginState,
) {
    companion object {
        fun admin(): PlayerSave {
            return PlayerSave(
                playerObjects = PlayerObjects.admin(),
                inventory = Inventory.admin(),
                neighborHistory = emptyMap(),
                loginState = PlayerLoginState.admin()
            )
        }

        fun newgame(pid: String, username: String): PlayerSave {
            return PlayerSave(
                playerObjects = PlayerObjects.newgame(pid, username),
                inventory = Inventory.newgame(),
                neighborHistory = emptyMap(),
                loginState = PlayerLoginState.newgame()
            )
        }
    }
}
