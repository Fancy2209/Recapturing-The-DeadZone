package dev.deadzone.core.data.assets

import dev.deadzone.module.Logger
import org.w3c.dom.Document
import org.w3c.dom.Element

class ItemsParser() : GameResourcesParser {
    override fun parse(doc: Document) {
        val items = doc.getElementsByTagName("item")

        for (i in 0 until items.length) {
            val item = items.item(i) as? Element ?: continue
            val itemType = item.getAttribute("type").takeIf { it != "" } ?: continue

            Logger.debug { "Found item of type=$itemType" }

            val subparser = when (itemType) {
                "weapon" -> WeaponItemParser()
                else -> GenericItemParser()
            }
            subparser.parse(item)
        }
    }
}

interface ItemSubparser {
    fun parse(el: Element)
}

class GenericItemParser(): ItemSubparser {
    override fun parse(el: Element) {

    }
}

class WeaponItemParser(): ItemSubparser {
    override fun parse(el: Element) {

    }
}
