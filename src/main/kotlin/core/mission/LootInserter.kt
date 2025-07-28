package dev.deadzone.core.mission

import dev.deadzone.module.Logger
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.StringReader
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.parsers.DocumentBuilder
import org.xml.sax.InputSource
import kotlin.random.Random

fun insertLoots(xmlInput: String): String {
    val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val doc: Document = docBuilder.parse(InputSource(StringReader(xmlInput)))

    val eList = doc.getElementsByTagName("e")
    for (i in 0 until eList.length) {
        val eNode = eList.item(i) as? Element ?: continue

        val optNode = eNode.getElementsByTagName("opt").item(0) as? Element ?: continue
        val srchNode = optNode.getElementsByTagName("srch").item(0) ?: continue

        val hasItms = (0 until eNode.childNodes.length)
            .map { eNode.childNodes.item(it) }
            .any { it is Element && it.tagName == "itms" }

        if (!hasItms) {
            Logger.socketPrint("Inserting itms for e with srch=${srchNode.textContent}")

            val itmsElement = doc.createElement("itms")

            val items = listOf(
                Triple("bladesaw-${Random.nextInt()}", "bladesaw", 1),
                Triple("hat-cowboy-usa-${Random.nextInt()}", "hat-cowboy-usa", 1),
                Triple("pain-kilers-${Random.nextInt()}", "pain-killers", (2..4).random()),
                Triple("fuel-bottle-${Random.nextInt()}", "fuel-bottle", (1..10).random())
            )

            for ((id, type, q) in items) {
                val itm = doc.createElement("itm")
                itm.setAttribute("id", id)
                itm.setAttribute("type", type)
                itm.setAttribute("q", q.toString())
                itmsElement.appendChild(itm)
            }

            eNode.appendChild(itmsElement)
        }
    }

    // Transform back to string
    val transformer = TransformerFactory.newInstance().newTransformer().apply {
        setOutputProperty(OutputKeys.INDENT, "yes")
        setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
    }

    val writer = StringWriter()
    transformer.transform(DOMSource(doc), StreamResult(writer))
    return writer.toString()
}
