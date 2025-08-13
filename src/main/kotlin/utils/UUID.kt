package dev.deadzone.utils

import java.util.UUID

object UUID {
    /**
     * Returns an uppercased UUID from java.util.uuid
     */
    fun new(): String {
        return UUID.randomUUID().toString().uppercase()
    }
}
