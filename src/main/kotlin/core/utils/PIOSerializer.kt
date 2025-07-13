package dev.deadzone.core.utils

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
            return values().sortedByDescending { it.value }.firstOrNull {
                byte and it.value == it.value
            } ?: DOES_NOT_EXIST
        }
    }
}

class PIOSerializer {
    private val buffer = mutableListOf<Byte>()

    fun serialize(message: List<Any>): ByteArray {
        buffer.clear()
        serializeValue(message.size - 1)
        for (value in message) {
            serializeValue(value)
        }
        return buffer.toByteArray()
    }

    private fun serializeValue(value: Any) {
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
                if (value in Int.MIN_VALUE..Int.MAX_VALUE) {
                    writeTagWithLength(value, Pattern.UNSIGNED_INT_SHORT_PATTERN, Pattern.INT_PATTERN)
                }
            }

            is Long -> {
                writeLongPattern(value, Pattern.LONG_SHORT_PATTERN, Pattern.LONG_PATTERN, signed = true)
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

    private fun writeTagWithLength(length: Int, shortPattern: Pattern, fullPattern: Pattern) {
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

    private fun writeLongPattern(value: Long, shortPattern: Pattern, longPattern: Pattern, signed: Boolean) {
        val encoded = reverseBytes(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value).array())
        val nonZero = encoded.indexOfFirst { it != 0.toByte() }.takeIf { it >= 0 } ?: 7
        val used = 8 - nonZero
        val pattern = if (used > 4) longPattern else shortPattern
        val offset = if (used > 4) used - 5 else used - 1
        buffer.add((pattern.value or offset).toByte())
        buffer.addAll(encoded.drop(nonZero))
    }

    private fun reverseBytes(bytes: ByteArray): ByteArray = bytes.reversedArray()
}


class PIODeserializer {
    private var state = "init"
    private var pattern = Pattern.DOES_NOT_EXIST
    private var buffer = ByteArrayOutputStream()
    private var partLength = 0
    private var length = -1
    private var message = mutableListOf<Any>()

    fun deserialize(data: ByteArray): List<Any> {
        reset()
        for (byte in data) {
            deserializeByte(byte)
        }
        return message.toList()
    }

    fun reset() {
        state = "init"
        pattern = Pattern.DOES_NOT_EXIST
        buffer.reset()
        partLength = 0
        length = -1
        message.clear()
    }

    private fun deserializeByte(byteVal: Byte) {
        when (state) {
            "init" -> handleInitState(byteVal)
            "header" -> {
                buffer.write(byteVal.toInt())
                if (buffer.size() == partLength) {
                    val reversed = buffer.toByteArray().reversedArray()
                    partLength = ByteBuffer.wrap(reversed.padStart(4)).order(ByteOrder.BIG_ENDIAN).int
                    buffer.reset()
                    state = "data"
                }
            }

            "data" -> {
                buffer.write(byteVal.toInt())
                if (buffer.size() == partLength) {
                    handlePatternData(buffer.toByteArray())
                    buffer.reset()
                    state = "init"
                }
            }
        }
    }

    private fun handleInitState(byteVal: Byte) {
        pattern = Pattern.fromByte(byteVal.toInt() and 0xFF)
        val part = byteVal.toInt() and 0x3F

        when (pattern) {
            Pattern.STRING_SHORT_PATTERN, Pattern.BYTE_ARRAY_SHORT_PATTERN -> {
                partLength = part
                state = if (partLength > 0) "data" else {
                    onValue(if (pattern == Pattern.STRING_SHORT_PATTERN) "" else ByteArray(0))
                    "init"
                }
            }

            Pattern.STRING_PATTERN, Pattern.BYTE_ARRAY_PATTERN,
            Pattern.UNSIGNED_INT_PATTERN, Pattern.INT_PATTERN -> {
                partLength = part + 1
                state = "header"
            }

            Pattern.UNSIGNED_INT_SHORT_PATTERN -> {
                onValue(part)
            }

            Pattern.UNSIGNED_LONG_SHORT_PATTERN, Pattern.LONG_SHORT_PATTERN -> {
                partLength = 1
                state = "data"
            }

            Pattern.UNSIGNED_LONG_PATTERN, Pattern.LONG_PATTERN -> {
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

            else -> {}
        }
    }

    private fun handlePatternData(bytes: ByteArray) {
        try {
            val padded = { b: ByteArray, size: Int -> b.padStart(size) }

            val result = when (pattern) {
                Pattern.STRING_SHORT_PATTERN,
                Pattern.STRING_PATTERN -> bytes.toString(Charsets.UTF_8)

                Pattern.UNSIGNED_INT_PATTERN -> ByteBuffer.wrap(padded(bytes, 4)).order(ByteOrder.BIG_ENDIAN).int
                Pattern.INT_PATTERN -> ByteBuffer.wrap(padded(bytes, 4)).order(ByteOrder.BIG_ENDIAN).int

                Pattern.UNSIGNED_LONG_PATTERN,
                Pattern.UNSIGNED_LONG_SHORT_PATTERN -> ByteBuffer.wrap(padded(bytes, 8))
                    .order(ByteOrder.BIG_ENDIAN).long

                Pattern.LONG_PATTERN,
                Pattern.LONG_SHORT_PATTERN -> ByteBuffer.wrap(padded(bytes, 8)).order(ByteOrder.BIG_ENDIAN).long

                Pattern.DOUBLE_PATTERN -> ByteBuffer.wrap(padded(bytes, 8)).order(ByteOrder.BIG_ENDIAN).double
                Pattern.FLOAT_PATTERN -> ByteBuffer.wrap(padded(bytes, 4)).order(ByteOrder.BIG_ENDIAN).float

                Pattern.BYTE_ARRAY_SHORT_PATTERN,
                Pattern.BYTE_ARRAY_PATTERN -> bytes

                else -> null
            }

            onValue(result)
        } catch (e: Exception) {
            println("Error deserializing pattern $pattern: ${e.message}")
            onValue(null)
        }
    }

    private fun onValue(value: Any?) {
        if (length == -1 && value is Int) {
            length = value
        } else {
            message.add(value ?: "null")
        }
    }

    private fun ByteArray.padStart(size: Int): ByteArray {
        return ByteArray(size - this.size) { 0 } + this
    }
}
