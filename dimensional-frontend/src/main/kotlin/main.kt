import kotlin.browser.*
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.coroutines.experimental.suspendCoroutine
import kotlin.dom.addClass
import kotlin.dom.removeClass

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
        var a = listOf(Plate(Segment(14.0, "cm", "Ca"), Segment()),
                Plate(Segment(54.0, "miles", "NaOH"), Segment(6.4, "L", "calcium")))
        document.body!!.append(Table(a).render(document.body!!, document))
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
        val div = document.create.div("table")
        plates.forEach { div.appendChild(it.render(parent, document)) }
        return div
    }
}

class Plate(val top : Segment, val bottom : Segment) : Renderable {
    override fun wrap() : String = "latex thing ${top.wrap()}, ${bottom.wrap()}"
    override fun render(parent: Element, document: Document) : HTMLElement {
        val div = document.create.div("plate") { + "plate" }
        div.appendChild(top.render(parent, document))
        div.appendChild(bottom.render(parent, document))
        /* div.addEventListener("mouseover", fun(event : Event) {
            event as MouseEvent
            div.addClass("red")
        })*/
        return div
    }
}
enum class State {
    VIEW, EDIT
}
class Segment (var number : Double = 0.0, var unit : String = "cm", var element : String = "Na") : Renderable {
    var state : State = State.VIEW
    constructor(input : String) : this () {
        val all = input.split("")
        this.number = all[0].toDoubleOrNull() ?: throw IllegalArgumentException("not a double")
        this.unit = all[1]
        this.element = all[2]
        //this class has html state because it needs to
    }

    override fun render(parent: Element, document: Document) : HTMLElement {
        var a = document.create.div("segment") { + "${number} , ${unit} , and ${element}" }
        a.addEventListener("click", { event->
            if (state == State.VIEW) {
                state = State.EDIT
                a.appendChild(document.create.textArea {
                    id = "editbox"
                    onKeyPressFunction = {event ->
                    event as KeyboardEvent
                    //enter key
                    if (event.charCode == 0) {
                        state = State.VIEW
                        val elem = document.querySelector("#editbox")!!
                        a.removeChild(elem)
                    }
                }
                })
            }
        })
        return a
    }

    override fun wrap() : String = "latext thing 1 $number $unit $element"
}