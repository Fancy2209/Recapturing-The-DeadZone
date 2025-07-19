package dev.deadzone.core.data

interface DataProvider {
    fun loadRawJson(path: String): String
}
