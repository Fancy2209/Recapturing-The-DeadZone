package dev.deadzone.socket

import io.ktor.network.sockets.Socket

data class Connection(
    val socket: Socket
) {
    override fun toString(): String {
        return "Socket: ${this.socket} | [ADDR]: ${this.socket.remoteAddress}"
    }
}
