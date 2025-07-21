package dev.deadzone.core.auth

import dev.deadzone.core.data.HardcodedData

object HardcodedAuthProvider : AuthProvider {
    override fun authenticate(token: String): String? {
        return if (token == HardcodedData.TOKEN) HardcodedData.USER_ID else null
    }
}
