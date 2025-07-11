package dev.deadzone.module

import io.ktor.server.application.*
import kotlinx.rpc.krpc.ktor.server.Krpc

fun Application.configureFrameworks() {
    install(Krpc)
}
