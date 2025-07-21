package dev.deadzone.core.data.game

interface DataProvider {
    fun loadRawJson(path: String): String
}
