package dev.deadzone.core.model.game.data

import dev.deadzone.module.LogConfigSocketToClient
import dev.deadzone.module.Logger
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = BuildingLikeSerializer::class)
@JsonClassDiscriminator("_t")
sealed class BuildingLike

@Serializable
data class Building(
    val id: String = UUID.randomUUID().toString(),    // building's unique ID
    val name: String? = null,
    val type: String,  // building's ID in buildings.xml, not to be confused with type in XML
    val level: Int = 0,
    val rotation: Int,
    val tx: Int,
    val ty: Int,
    val destroyed: Boolean = false,
    val resourceValue: Double = 0.0,
    val upgrade: TimerData? = null,
    val repair: TimerData? = null
) : BuildingLike()

object BuildingLikeSerializer : JsonContentPolymorphicSerializer<BuildingLike>(BuildingLike::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BuildingLike> {
        val discriminator = element.jsonObject["_t"]?.jsonPrimitive?.contentOrNull

        return when (discriminator) {
            "dev.deadzone.core.model.game.data.Building" -> Building.serializer()
            "dev.deadzone.core.model.game.data.JunkBuilding" -> JunkBuilding.serializer()
            null -> {
                val obj = element.jsonObject
                return when {
                    obj.containsKey("items") && obj.containsKey("pos") && obj.containsKey("rot") ->
                        JunkBuilding.serializer()

                    else ->
                        Building.serializer()
                }
            }

            else -> {
                Logger.error(
                    LogConfigSocketToClient,
                    forceLogFull = true
                ) { "Error during serialization of BuildingLike type: $element" }
                throw SerializationException("Unknown type: '$discriminator'")
            }
        }
    }
}
