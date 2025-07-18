package dev.deadzone.core.model.data

import kotlinx.serialization.Serializable

@Serializable
data class FlagSet(
    val byteArray: ByteArray = byteArrayOf()
)
