package dev.deadzone.module

import dev.deadzone.core.model.game.data.Building
import dev.deadzone.core.model.game.data.BuildingLike
import dev.deadzone.core.model.game.data.JunkBuilding
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.protobuf.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        val module = SerializersModule {
            polymorphic(BuildingLike::class) {
                subclass(Building::class, Building.serializer())
                subclass(JunkBuilding::class, JunkBuilding.serializer())
            }
        }

        val json = Json {
            serializersModule = module
            classDiscriminator = "_t"
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
        Dependency.json = json
        json(json)
        protobuf(ProtoBuf)
    }
}
