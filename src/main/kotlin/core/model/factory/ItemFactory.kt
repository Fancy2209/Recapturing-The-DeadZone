package dev.deadzone.core.model.factory

import dev.deadzone.core.data.assets.ItemResource
import dev.deadzone.core.model.game.data.Item
import dev.deadzone.core.model.game.data.ItemQualityType
import dev.deadzone.core.model.game.data.ItemQualityType_Constants
import dev.deadzone.module.Dependency
import org.w3c.dom.Element

object ItemFactory {
    private val gameData get() = Dependency.gameData

    fun createItemFromId(id: String): Item {
        val res =
            gameData.itemsById[id] ?: throw IllegalArgumentException("Failed creating Item from id=$id (id not found)")
        return createItemFromResource(res)
    }

    fun createItemFromResource(res: ItemResource): Item {
        val baseItem = Item(
            id = res.id,
            type = res.type,
            quality = ItemQualityType.fromString(res.element.getAttribute("quality").ifBlank { "none" })
        )

        when (res.type) {
            "gear" -> parseGear(res.element, baseItem)
            "weapon" -> parseWeapon(res.element, baseItem)
            "clothing" -> parseClothing(res.element, baseItem)
            // and many more...
        }

        return baseItem
    }

    // these should modify the base item as needed
    // currently does nothing as we are not sure the detailed traits of each item types
    private fun parseGear(element: Element, baseItem: Item) {}
    private fun parseWeapon(element: Element, baseItem: Item) {}
    private fun parseClothing(element: Element, baseItem: Item) {}
}
