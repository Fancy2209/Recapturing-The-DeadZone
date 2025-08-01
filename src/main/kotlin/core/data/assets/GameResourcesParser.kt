package dev.deadzone.core.data.assets

import org.w3c.dom.Document

interface GameResourcesParser {
    fun parse(doc: Document)
}
