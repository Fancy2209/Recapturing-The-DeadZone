package dev.deadzone.config

import java.io.File

object GamePaths {
    val BASE_PATH = File("resources/static/game/data")

    val XML_DIR = BASE_PATH.resolve("xml")
}
