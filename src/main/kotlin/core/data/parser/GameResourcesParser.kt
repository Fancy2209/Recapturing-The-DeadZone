package dev.deadzone.core.data.assets

import dev.deadzone.core.data.GameResourceRegistry
import org.w3c.dom.Document

/**
 * Parser for the game XML resources (e.g., `items.xml`, `zombies.xml`)
 *
 * This is used to create code level representation from the game's data.
 *
 * As an example, [ItemsParser] reads the `items.xml` and depending on the item type
 * (e.g., `type="weapon"`, `type="junk"`), it chooses subparser (i.e., [WeaponItemParser])
 * and creates the corresponding [dev.deadzone.core.model.game.data.Item] object.
 */
interface GameResourcesParser {
    fun parse(doc: Document, gameResourceRegistry: GameResourceRegistry)
}
