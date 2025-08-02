package dev.deadzone.core.model.factory

import dev.deadzone.core.data.assets.ItemResource
import dev.deadzone.core.model.game.data.Item
import dev.deadzone.core.model.game.data.ItemQualityType
import dev.deadzone.core.model.game.data.ItemQualityType_Constants
import dev.deadzone.module.Dependency
import org.w3c.dom.Element
import java.util.UUID

object ItemFactory {
    private val gameData get() = Dependency.gameData

    fun getRandomItem(): Item {
        return createItemFromResource(res = gameData.itemsById.values.random())
    }

    fun createItemFromId(itemId: String = UUID.randomUUID().toString(), idInXML: String): Item {
        val res =
            gameData.itemsById[idInXML]
                ?: throw IllegalArgumentException("Failed creating Item id=$itemId from xml id=$idInXML (xml id not found)")
        return createItemFromResource(itemId, res)
    }

    fun createItemFromResource(itemId: String = UUID.randomUUID().toString(), res: ItemResource): Item {
        val baseItem = Item(
            id = itemId,
            type = res.id,
            quality = ItemQualityType.fromString(res.element.getAttribute("quality"))
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
