import kotlin.browser.*
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

fun main(args: Array<String>) {
    window.onload = {
        fetch("thing")
        val input = document.getElementById("count_id")!!
        val button = document.getElementById("button_id")!!
        button.addEventListener("click", fun(event : Event) {
            println(event)
        })
    }

    window.onload = {
        document.body!!.append(Table(listOf(Plate(Segment(14.0, "cm", "Ca"), Segment()))).render(document.body!!, document))
    }
}

fun fetch(count : String) {
    var url = "https://localhost:8080/api/ping/$count"
}

interface Renderable {
    fun wrap() : String
    fun render(parent: Element, document: Document) : HTMLElement
}

data class Table(val plates : List<Plate>) : Renderable {
    override fun wrap() : String = "latexthing ${plates.forEach { it.wrap() }}"
    override  fun render(parent : Element, document : Document) : HTMLElement  {
        val div = document.create.div("table") {
            + "inside the table" }
        plates.forEach { div.appendChild(it.render(parent, document)) }
        return div
    }
}

class Plate(val top : Segment, val bottom : Segment) : Renderable {
    override fun wrap() : String = "latex dthing ${top.wrap()}, ${bottom.wrap()}"
    override fun render(parent: Element, document: Document) : HTMLElement {
        val div = document.create.div("plate") { + "plate" }
        div.appendChild(top.render(parent, document))
        div.appendChild(bottom.render(parent, document))
        return div
    }
}

class Segment (var number : Double = 0.0, var unit : String = "cm", var element : String = "Na") : Renderable {
    constructor(input : String) : this () {
        val all = input.split("")
        this.number = all[0].toDoubleOrNull() ?: throw IllegalArgumentException("not a double")
        this.unit = all[1]
        this.element = all[2]
    }

    override fun render(parent: Element, document: Document) : HTMLElement {
        return document.create.div("segment") { + "${number} , ${unit} , and ${element}"}
    }

    override fun wrap() : String = "latext thing 1 $number $unit $element"
}