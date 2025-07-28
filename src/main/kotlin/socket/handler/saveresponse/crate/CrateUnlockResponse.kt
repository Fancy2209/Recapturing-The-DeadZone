package dev.deadzone.socket.handler.saveresponse.crate

import dev.deadzone.core.model.game.data.Item
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CrateUnlockResponse(
    val success: Boolean = true,
    val item: Item = gachaExample(),
    val effect: String? = null,   // base64 encoded, parsed to [Effect]
    val cooldown: String? = null, // similar as above
    val keyId: String? = null, // to remove the key used to open the crate
    val keyQty: Int? = null, // to remove the key used to open the crate
    val crateId: String? = null, // to remove the opened crate
)

fun gachaExample(): Item {
    val items = listOf(
        Item(id = UUID.randomUUID().toString(), type = "exo-rig-heavyBrawler-replica", level = 44, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "helmet-exo-brawler-replica", level = 29, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "helmet-exo-targeting-replica", level = 29, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "mask-herc-exo-replica", level = 30, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "exo-undershirt-1-replica", level = 0, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "exo-underpants-1-replica", level = 0, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "pistol-halloween-reborn", level = 24, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "pistol-halloween-2-reborn", level = 44, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "rifle-halloween-reborn", level = 24, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "rifle-halloween-2-reborn", level = 49, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "sword-laser-purple-reborn", level = 1, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "trident-halloween-reborn", level = 34, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "trident-halloween-2-reborn", level = 54, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "crossbow-halloween-2015-reborn", level = 24, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "crossbow-halloween-2015-2-reborn", level = 49, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "halloween-exo-zombie", level = 0, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "bladesaw", level = 54, qty = 1u),
        Item(id = UUID.randomUUID().toString(), type = "warclub", level = 54, qty = 1u)
    )

    return items.random()
}
