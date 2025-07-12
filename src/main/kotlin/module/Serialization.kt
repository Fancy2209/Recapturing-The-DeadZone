package dev.deadzone.module

import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.protobuf.*
import io.ktor.server.application.Application
import kotlinx.serialization.protobuf.*
import io.ktor.server.application.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        protobuf(ProtoBuf {
            encodeDefaults = false
        })
    }
}
