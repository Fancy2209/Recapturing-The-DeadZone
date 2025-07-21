package dev.deadzone.core.auth

interface AuthProvider {
    fun authenticate(token: String): String? // userId
}
