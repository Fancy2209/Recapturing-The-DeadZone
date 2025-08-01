package dev.deadzone.module

import dev.deadzone.core.data.assets.*
import dev.deadzone.core.model.game.data.Item
import java.util.zip.GZIPInputStream
import javax.xml.parsers.DocumentBuilderFactory

class GameData {
    val items: List<Item> = listOf()

    init {
        val resourcesToLoad = mapOf(
            "static/game/data/xml/alliances.xml.gz" to AlliancesParser(),
            "static/game/data/xml/arenas.xml.gz" to ArenasParser(),
            "static/game/data/xml/attire.xml.gz" to AttireParser(),
            "static/game/data/xml/badwords.xml.gz" to BadwordsParser(),
            "static/game/data/xml/buildings.xml.gz" to BuildingsParser(),
            "static/game/data/xml/config.xml.gz" to ConfigParser(),
            "static/game/data/xml/crafting.xml.gz" to CraftingParser(),
            "static/game/data/xml/effects.xml.gz" to EffectsParser(),
            "static/game/data/xml/humanenemies.xml.gz" to HumanEnemiesParser(),
            "static/game/data/xml/injury.xml.gz" to InjuryParser(),
            "static/game/data/xml/itemmods.xml.gz" to ItemModsParser(),
            "static/game/data/xml/items.xml.gz" to ItemsParser(),
            "static/game/data/xml/quests.xml.gz" to QuestsGlobalParser(),
            "static/game/data/xml/quests_global.xml.gz" to RaidsParser(),
            "static/game/data/xml/raids.xml.gz" to SkillsParser(),
            "static/game/data/xml/skills.xml.gz" to StreetStructsParser(),
            "static/game/data/xml/streetstructs.xml.gz" to SurvivorParser(),
            "static/game/data/xml/survivor.xml.gz" to VehicleNamesParser(),
            "static/game/data/xml/vehiclenames.xml.gz" to ZombiesParser()
        )

        val classLoader = Thread.currentThread().contextClassLoader

        for ((path, parser) in resourcesToLoad) {
            val inputStream = classLoader.getResourceAsStream(path)
                ?: throw IllegalStateException("File not found in resources: $path")

            if (path != "static/game/data/xml/items.xml.gz") continue // only parse items for now

            val xmlStream = GZIPInputStream(inputStream)
            val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlStream)

            parser.parse(document)
        }

    }
}
