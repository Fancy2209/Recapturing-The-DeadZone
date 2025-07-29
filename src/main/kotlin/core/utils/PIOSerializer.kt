package dev.deadzone.core.utils

import dev.deadzone.module.Logger
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal enum class Pattern(val value: Int) {
    STRING_SHORT_PATTERN(0xC0),
    STRING_PATTERN(0x0C),
    BYTE_ARRAY_SHORT_PATTERN(0x40),
    BYTE_ARRAY_PATTERN(0x10),
    UNSIGNED_LONG_SHORT_PATTERN(0x38),
    UNSIGNED_LONG_PATTERN(0x3C),
    LONG_SHORT_PATTERN(0x30),
    LONG_PATTERN(0x34),
    UNSIGNED_INT_SHORT_PATTERN(0x80),
    UNSIGNED_INT_PATTERN(0x08),
    INT_PATTERN(0x04),
    DOUBLE_PATTERN(0x03),
    FLOAT_PATTERN(0x02),
    BOOLEAN_TRUE_PATTERN(0x01),
    BOOLEAN_FALSE_PATTERN(0x00),
    DOES_NOT_EXIST(0xFF);

    companion object {
        fun fromByte(byte: Int): Pattern {
            return entries.toTypedArray().sortedByDescending { it.value }.firstOrNull {
                byte and it.value == it.value
            } ?: DOES_NOT_EXIST
        }
    }
}

object PIOSerializer {
    fun serialize(message: List<Any>): ByteArray {
        val buffer = mutableListOf<Byte>()

        fun reverseBytes(bytes: ByteArray): ByteArray = bytes.reversedArray()

        fun writeTagWithLength(length: Int, shortPattern: Pattern, fullPattern: Pattern) {
            if (length in 0..63) {
                buffer.add((shortPattern.value or length).toByte())
            } else {
                val encoded = reverseBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(length).array())
                val nonZero = encoded.indexOfFirst { it != 0.toByte() }.takeIf { it >= 0 } ?: 3
                val used = 4 - nonZero
                buffer.add((fullPattern.value or (used - 1)).toByte())
                buffer.addAll(encoded.drop(nonZero))
            }
        }

        fun writeLongPattern(value: Long, shortPattern: Pattern, longPattern: Pattern) {
            val encoded = reverseBytes(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value).array())
            val nonZero = encoded.indexOfFirst { it != 0.toByte() }.takeIf { it >= 0 } ?: 7
            val used = 8 - nonZero
            val pattern = if (used > 4) longPattern else shortPattern
            val offset = if (used > 4) used - 5 else used - 1
            buffer.add((pattern.value or offset).toByte())
            buffer.addAll(encoded.drop(nonZero))
        }

        fun serializeValue(value: Any) {
            when (value) {
                is String -> {
                    val encoded = value.toByteArray(Charsets.UTF_8)
                    writeTagWithLength(encoded.size, Pattern.STRING_SHORT_PATTERN, Pattern.STRING_PATTERN)
                    buffer.addAll(encoded.toList())
                }

                is Boolean -> {
                    buffer.add(if (value) Pattern.BOOLEAN_TRUE_PATTERN.value.toByte() else Pattern.BOOLEAN_FALSE_PATTERN.value.toByte())
                }

                is Int -> {
                    writeTagWithLength(value, Pattern.UNSIGNED_INT_SHORT_PATTERN, Pattern.INT_PATTERN)
                }

                is Long -> {
                    writeLongPattern(value, Pattern.LONG_SHORT_PATTERN, Pattern.LONG_PATTERN)
                }

                is Float -> {
                    buffer.add(Pattern.DOUBLE_PATTERN.value.toByte())
                    buffer.addAll(
                        reverseBytes(
                            ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(value.toDouble()).array()
                        ).toList()
                    )
                }

                is Double -> {
                    buffer.add(Pattern.DOUBLE_PATTERN.value.toByte())
                    buffer.addAll(
                        reverseBytes(
                            ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(value).array()
                        ).toList()
                    )
                }

                is ByteArray -> {
                    writeTagWithLength(value.size, Pattern.BYTE_ARRAY_SHORT_PATTERN, Pattern.BYTE_ARRAY_PATTERN)
                    buffer.addAll(value.toList())
                }

                else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
            }
        }

        serializeValue(message.size - 1)
        for (value in message) {
            serializeValue(value)
        }

        return buffer.toByteArray()
    }
}

object PIODeserializer {
    fun deserialize(data: ByteArray): List<Any> {
        var state = "init"
        var pattern = Pattern.DOES_NOT_EXIST
        val buffer = ByteArrayOutputStream()
        var partLength = 0
        var length = -1
        val message = mutableListOf<Any>()

        fun onValue(value: Any?) {
            if (length == -1 && value is Int) {
                length = value
            } else {
                message.add(value ?: "null")
            }
        }

        fun ByteArray.padStart(size: Int): ByteArray {
            return ByteArray(size - this.size) { 0 } + this
        }

        try {
            for (byte in data) {
                when (state) {
                    "init" -> {
                        pattern = Pattern.fromByte(byte.toInt() and 0xFF)
                        val part = byte.toInt() and 0x3F

                        when (pattern) {
                            Pattern.STRING_SHORT_PATTERN,
                            Pattern.BYTE_ARRAY_SHORT_PATTERN -> {
                                partLength = part
                                state = if (partLength > 0) "data" else {
                                    val value = if (pattern == Pattern.STRING_SHORT_PATTERN) "" else ByteArray(0)
                                    onValue(value)
                                    "init"
                                }
                            }

                            Pattern.STRING_PATTERN,
                            Pattern.BYTE_ARRAY_PATTERN,
                            Pattern.UNSIGNED_INT_PATTERN,
                            Pattern.INT_PATTERN -> {
                                partLength = 4
                                state = "header"
                            }

                            Pattern.UNSIGNED_INT_SHORT_PATTERN -> {
                                onValue(part)
                            }

                            Pattern.UNSIGNED_LONG_SHORT_PATTERN,
                            Pattern.LONG_SHORT_PATTERN -> {
                                partLength = 1
                                state = "data"
                            }

                            Pattern.UNSIGNED_LONG_PATTERN,
                            Pattern.LONG_PATTERN -> {
                                partLength = 6
                                state = "data"
                            }

                            Pattern.DOUBLE_PATTERN -> {
                                partLength = 8
                                state = "data"
                            }

                            Pattern.FLOAT_PATTERN -> {
                                partLength = 4
                                state = "data"
                            }

                            Pattern.BOOLEAN_TRUE_PATTERN -> onValue(true)
                            Pattern.BOOLEAN_FALSE_PATTERN -> onValue(false)
                            else -> {} // unsupported
                        }
                    }

                    "header" -> {
                        buffer.write(byte.toInt())
                        if (buffer.size() == partLength) {
                            val padded = buffer.toByteArray().padStart(4)
                            partLength = ByteBuffer.wrap(padded).order(ByteOrder.LITTLE_ENDIAN).int

                            if (partLength < 0 || partLength > 10_000_000) {
                                throw IllegalArgumentException("Invalid partLength = $partLength")
                            }

                            buffer.reset()
                            state = "data"
                        }
                    }

                    "data" -> {
                        buffer.write(byte.toInt())
                        if (buffer.size() == partLength) {
                            val bytes = buffer.toByteArray()
                            val padded = { b: ByteArray, size: Int -> b.padStart(size) }

                            val value = try {
                                when (pattern) {
                                    Pattern.STRING_SHORT_PATTERN,
                                    Pattern.STRING_PATTERN -> bytes.toString(Charsets.UTF_8)

                                    Pattern.UNSIGNED_INT_PATTERN,
                                    Pattern.INT_PATTERN -> ByteBuffer.wrap(padded(bytes, 4)).order(ByteOrder.BIG_ENDIAN).int

                                    Pattern.UNSIGNED_LONG_PATTERN,
                                    Pattern.UNSIGNED_LONG_SHORT_PATTERN,
                                    Pattern.LONG_PATTERN,
                                    Pattern.LONG_SHORT_PATTERN -> ByteBuffer.wrap(padded(bytes, 8)).order(ByteOrder.BIG_ENDIAN).long

                                    Pattern.DOUBLE_PATTERN -> ByteBuffer.wrap(padded(bytes, 8)).order(ByteOrder.BIG_ENDIAN).double

                                    Pattern.FLOAT_PATTERN -> ByteBuffer.wrap(padded(bytes, 4)).order(ByteOrder.BIG_ENDIAN).float

                                    Pattern.BYTE_ARRAY_SHORT_PATTERN,
                                    Pattern.BYTE_ARRAY_PATTERN -> bytes

                                    else -> null
                                }
                            } catch (e: Exception) {
                                Logger.print("Error deserializing pattern $pattern: ${e.message}")
                                null
                            }

                            onValue(value)
                            buffer.reset()
                            state = "init"
                        }
                    }
                }
            }

            return message
        } catch (_: Exception) {
            Logger.print("Deserializer receives JSON-like message")
        }

        val offset = data.indexOfFirst { it == '{'.code.toByte() }

        return if (offset != -1) {
            try {
                val jsonBytes = data.copyOfRange(offset, data.size)
                val json = jsonBytes.toString(Charsets.UTF_8)
                val parsed = parseJsonToMap(json)
                val type = message.firstOrNull() as? String

                if (type != null) {
                    val isAlreadyWrapped = parsed.size == 1 && parsed.containsKey(type)

                    val final = if (isAlreadyWrapped) {
                        listOf(type, parsed[type]!!)
                    } else {
                        listOf(type, parsed)
                    }
                    final
                } else {
                    Logger.print("Cannot determine message type from partial data")
                    emptyList()
                }
            } catch (e: Exception) {
                Logger.print("JSON fallback deserialization failed: ${e.message}")
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}


fun parseJsonToMap(json: String): Map<String, Any?> {
    return try {
        val parsed = Json.decodeFromString<JsonObject>(json)
        parsed.mapValues { (_, v) -> parseJsonElement(v) }
    } catch (_: Exception) {
        emptyMap()
    }
}

fun parseJsonElement(el: JsonElement): Any? = when (el) {
    is JsonPrimitive -> {
        when {
            el.isString -> el.content
            el.booleanOrNull != null -> el.boolean
            el.intOrNull != null -> el.int
            el.longOrNull != null -> el.long
            el.doubleOrNull != null -> el.double
            else -> el.content
        }
    }
    is JsonObject -> el.mapValues { parseJsonElement(it.value) }
    is JsonArray -> el.map { parseJsonElement(it) }
}
