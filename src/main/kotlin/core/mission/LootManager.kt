package dev.deadzone.core.mission

import dev.deadzone.core.data.assets.ItemResource
import dev.deadzone.core.model.factory.ItemFactory
import dev.deadzone.core.model.game.data.Item
import dev.deadzone.module.Dependency
import dev.deadzone.module.LogSource
import dev.deadzone.module.Logger
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.StringReader
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.parsers.DocumentBuilder
import org.xml.sax.InputSource
import java.util.UUID
import kotlin.collections.emptyMap
import kotlin.random.Random

val ALL_LOCS = listOf(
    "crafting", "backpack", "kitchen", "lounge", "bedroom", "bathroom",
    "random", "office", "store", "military", "weapon", "resource", "police",
    "gear", "tutorial", "fuel", "food", "fridge", "water", "vending", "car",
    "body", "basement", "ammo", "wood", "metal", "cloth", "comms", "hospital",
    "island", "firstaid"
) // + the tutorial convenience store (tutorialfuel)

data class LootContent(
    val itemId: String,
    val itemIdInXML: String,
    val quantity: Int,
)

// Loot system config, contains variables that affects loot
data class LootParameter(
    val areaLevel: Int,
    val playerLevel: Int,
    val itemWeightOverrides: Map<String, Double>,
    val itemTypeMultiplier: Map<String, Double>,
    val itemQualityMultiplier: Map<String, Double>,
    val baseWeight: Double,
    val fuelLimit: Int,
)

class LootManager(
    private val sceneXML: String,
    private val parameter: LootParameter
) {
    val lootableItemsOnEachLocs: MutableMap<String, MutableList<Pair<LootContent, Double>>> = mutableMapOf()
    val insertedLoots: MutableList<LootContent> = mutableListOf()
    val itemsLootedByPlayer: MutableList<LootContent> = mutableListOf()

    init {
        buildIndexOfLootableItems()
    }

    private fun buildIndexOfLootableItems() {
        ALL_LOCS.forEach { loc ->
            val lootableInLoc = Dependency.gameData.itemsByLootable[loc] ?: emptyList()

            for (item in lootableInLoc) {
                val lvlMin = item.element.getElementsByTagName("lvl_min").item(0)?.textContent?.toIntOrNull() ?: 0
                val lvlMax = item.element.getElementsByTagName("lvl_max").item(0)?.textContent?.toIntOrNull()
                    ?: Int.MAX_VALUE
                if (parameter.areaLevel !in (lvlMin..lvlMax)) continue

                // get the item rarity, type, quality, and assign a weight
                val rarity = item.element.getAttribute("rarity").toIntOrNull() ?: 0

                val type = item.element.getAttribute("type").ifBlank { "unknown" }
                val typeMultiplier = parameter.itemTypeMultiplier[type] ?: 1.0

                val quality = item.element.getAttribute("quality").ifBlank { "common" }
                val qualityMultiplier = parameter.itemQualityMultiplier[quality] ?: 1.0

                val baseWeight = 1000.0 / (rarity.coerceAtLeast(1))
                val finalWeight =
                    (parameter.itemWeightOverrides[item.id] ?: baseWeight) * typeMultiplier * qualityMultiplier

                lootableItemsOnEachLocs.computeIfAbsent(loc) { mutableListOf() }.add(
                    createLootContent(item) to finalWeight
                )
            }
        }
    }

    fun insertLoots(): String {
        val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc: Document = docBuilder.parse(InputSource(StringReader(sceneXML)))

        val eList = doc.getElementsByTagName("e")
        for (i in 0 until eList.length) {
            val eNode = eList.item(i) as? Element ?: continue

            val optNode = eNode.getElementsByTagName("opt").item(0) as? Element ?: continue
            val srchNode = optNode.getElementsByTagName("srch").item(0) ?: continue

            // skip if the corresponding node has a predefined itms
            val hasItms = (0 until eNode.childNodes.length)
                .map { eNode.childNodes.item(it) }
                .any { it is Element && it.tagName == "itms" }

            if (!hasItms) {
                val itmsElement = doc.createElement("itms")

                val loots = getRollsFromLocs(srchNode.textContent.split(","))
                for ((id, type, q) in loots) {
                    val itm = doc.createElement("itm")
                    itm.setAttribute("id", id)
                    itm.setAttribute("type", type)
                    itm.setAttribute("q", q.toString())
                    itmsElement.appendChild(itm)
                }

                insertedLoots.addAll(loots)
                eNode.appendChild(itmsElement)
            }
        }

        // Transform back to string
        val transformer = TransformerFactory.newInstance().newTransformer().apply {
            setOutputProperty(OutputKeys.INDENT, "yes")
            setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        }

        val writer = StringWriter()
        transformer.transform(DOMSource(doc), StreamResult(writer))
        return writer.toString()
    }

    private fun getRollsFromLocs(locs: List<String>): List<LootContent> {
        // roll 0-4 items per container
        val lootsAmount = (0..4).random()
        var remainingLoot = lootsAmount
        val lootResults: MutableList<LootContent> = mutableListOf()

        // one item per shuffled locs
        val shuffledLocs = locs.shuffled()
        var i = 0

        while (remainingLoot > 0) {
            val loc = shuffledLocs[i % shuffledLocs.size]
            val lootCandidates = lootableItemsOnEachLocs[loc] ?: emptyList()
            if (lootCandidates.isEmpty()) return emptyList()

            val chosen = weightedRandom(lootCandidates)
            chosen?.let { lootResults.add(it) }
            remainingLoot -= 1
            i += 1
        }

        return lootResults
    }

    private fun weightedRandom(choices: List<Pair<LootContent, Double>>): LootContent? {
        // sum all weights and pick a random within the weight range
        // item with higher weight (more common) is more likely to be picked
        val totalWeight = choices.sumOf { it.second }
        var rand = Math.random() * totalWeight

        for ((lootContent, weight) in choices) {
            rand -= weight
            if (rand <= 0) return lootContent
        }

        return choices.lastOrNull()?.first
    }

    private fun createLootContent(res: ItemResource): LootContent {
        val element = res.element
        val qntMin = element.getElementsByTagName("qnt_min").item(0)?.textContent?.toIntOrNull() ?: 1
        val qntMax = element.getElementsByTagName("qnt_max").item(0)?.textContent?.toIntOrNull() ?: 1
        val min = minOf(qntMin, qntMax)
        val max = maxOf(qntMin, qntMax)
        val qty = (min..max).random()

        return LootContent(
            itemId = UUID.randomUUID().toString(),
            itemIdInXML = res.id,
            quantity = qty,
        )
    }
}
