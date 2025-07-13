package dev.deadzone.module

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.protobuf.*
import io.ktor.server.application.Application
import kotlinx.serialization.protobuf.*
import io.ktor.server.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
        protobuf(ProtoBuf {
            encodeDefaults = false
        })
    }
}
