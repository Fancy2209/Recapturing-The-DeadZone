package dev.deadzone.core.utils

class Message(private val raw: List<Any>) {
    val type: String? = if (raw.size % 2 == 1) raw.firstOrNull() as? String else null

    private val map: Map<String, Any?> = buildMap {
        val start = if (type != null) 1 else 0
        val end = raw.size
        for (i in start until end step 2) {
            val key = raw.getOrNull(i) as? String ?: continue
            val value = raw.getOrNull(i + 1)
            put(key, value)
        }
    }

    fun get(key: String): Any? = map[key]

    fun getString(key: String): String? = map[key] as? String
    fun getInt(key: String): Int? = (map[key] as? Number)?.toInt()
    fun getBoolean(key: String): Boolean? = map[key] as? Boolean
    fun getBytes(key: String): ByteArray? = map[key] as? ByteArray

    fun keys(): Set<String> = map.keys
    fun values(): Collection<Any?> = map.values

    override fun toString(): String = if (type != null)
        "Message(type=$type, map=$map)"
    else
        "Message(map=$map)"

    companion object {
        fun fromRaw(raw: List<Any>): Message {
            return Message(raw)
        }
    }
}
