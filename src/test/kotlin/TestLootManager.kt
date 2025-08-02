import dev.deadzone.core.mission.LootManager
import dev.deadzone.core.mission.LootParameter
import dev.deadzone.module.GameData
import dev.deadzone.socket.handler.saveresponse.mission.loadSceneXML
import java.io.File
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.GZIPInputStream
import kotlin.test.Test

object TestDependency {
    val gameData: GameData = GameData({})
}

class TestLootManager {
    private val PARAMETER1 = LootParameter(
        areaLevel = 30, // set this manually
        playerLevel = 30,
        itemWeightOverrides = mapOf(
            "fuel" to 50.0
        ),
        itemTypeMultiplier = mapOf(
            "junk" to 5.0
        ),
        itemQualityMultiplier = mapOf(
            "blue" to 5.0
        ),
        baseWeight = 1000.0,
        fuelLimit = 50
    )
    private val SCENES_TO_TEST = listOf("exterior-bridge-1")
    private val GENERATION_PER_SCENE = 10

    val logDir = File("logs")
    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
    val logFile = File(logDir, "loot_generation_test-$timestamp.log")

    @Test
    fun testLootGeneration() {
        if (!logDir.exists()) logDir.mkdirs()

        logFile.bufferedWriter().use { writer ->
            SCENES_TO_TEST.forEach { filename ->

                repeat(GENERATION_PER_SCENE) { i ->
                    val sceneXML = loadSceneXML(filename)
                    val manager = LootManager(
                        gameData = TestDependency.gameData,
                        sceneXML = sceneXML,
                        parameter = PARAMETER1
                    )
                    manager.insertLoots()

                    writer.write("========> Scene: $filename (iteration:$i\n")
                    manager.insertedLoots.forEach { loot ->
                        writer.write(" - ${loot.itemIdInXML} (x${loot.quantity})\n")
                    }
                    writer.write("==============================================\n")
                }
            }
        }

        println("Loot generation logs written to: ${logFile.absolutePath}")
    }
}

