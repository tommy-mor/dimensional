import kotlin.browser.*
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.*
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.coroutines.experimental.suspendCoroutine
import kotlin.dom.addClass
import kotlin.dom.removeClass
import kotlin.reflect.KProperty

//TODO get rid of document render param
//TODO convert to {event-> ...} notation not fun(event: Event) {...}

var tableList = mutableListOf(Plate(Segment(),Segment()))
var table = Table(tableList)
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
        document.body!!.append(table.render(document.body!!, document))
    }
}

fun fetch(count : String) {
    var url = "https://localhost:8080/api/ping/$count"
}

interface Renderable {
    fun wrap() : String
    fun render(parent: Element, document: Document) : HTMLElement
}

data class Table(val plates : MutableList<Plate>) : Renderable {
    val div = document.create.div("table")
    fun appendAnotherExpansionSlot() {
        var newOne = Plate(Segment(), Segment())
        println(plates.last().bottom.state)
        println(plates.last().top.state)
        if (!plates.last().isEmpty()) {
            plates.add(newOne)
            div.append(newOne.render(div, document))
        }
    }

    override fun wrap() : String = "latexthing ${plates.forEach { it.wrap() }}"
    override  fun render(parent : Element, document : Document) : HTMLElement  {
        div.id = "tableId"
        plates.forEach { div.appendChild(it.render(div, document)) }
        return div
    }
}

class Plate(val top : Segment, val bottom : Segment) : Renderable {
    override fun wrap() : String = "latex thing ${top.wrap()}, ${bottom.wrap()}"

    override fun render(parent: Element, document: Document) : HTMLElement {
        val div = document.create.div("plate") { + "plate" }
        div.appendChild(top.render(div, document))
        div.appendChild(bottom.render(div, document))
        /* div.addEventListener("mouseover", fun(event : Event) {
            event as MouseEvent
            div.addClass("red")
        })*/
        return div
    }

    fun isEmpty() : Boolean {
        return top.state == State.NEW && bottom.state == State.NEW
    }
}
enum class State {
    VIEW, EDIT, NEW
}


class Segment (var number : Double = 0.0, var unit : String = "Unit", var element : String = "Element") : Renderable {
    var state : State = State.NEW

    fun updateOnString(input : String) {
        val all = input.split(",")
        println(all)
        this.number = all[0].toDoubleOrNull() ?: throw IllegalArgumentException("not a double")
        this.unit = all[1]
        this.element = all[2]

    }

    override fun render(parent: Element, document: Document) : HTMLElement {
        var topDiv = document.create.div("segment")

        if(state == State.NEW)  topDiv.appendChild(document.create.div {
            id="helpmsg"
            +"CLICK TO EDIT"
        })

        var textDiv = document.create.div { id="displaybox"
            +"$number $unit $element"}

        if (state != State.NEW) topDiv.appendChild(textDiv)

        topDiv.addEventListener("click", { event->
            if (state == State.NEW) {
                table.appendAnotherExpansionSlot()
            }
            if (state == State.VIEW || state == State.NEW) {
                if (state == State.NEW) topDiv.removeChild(parent.querySelector("#helpmsg")!!)
                else {
                    topDiv.removeChild(textDiv)
                }

                state = State.EDIT

                topDiv.appendChild(document.create.textArea { id = "editbox"
                    onKeyPressFunction = {event ->
                        event as KeyboardEvent
                        //is 0 on mac, 13 on linux. TODO test windows/find real solution
                        if (event.charCode == 0 || event.charCode == 13) {
                            val editbox = parent.querySelector("#editbox")!! as HTMLTextAreaElement
                            updateOnString(editbox.value)
                            state = State.VIEW
                            topDiv.removeChild(editbox)
                            textDiv.innerHTML = "$number $unit $element"
                            topDiv.appendChild(textDiv)
                        }
                    }
                    + "$number, $unit, $element"
                })
            }
        })

        //TODO add button for toggling crossed out status (temp cause it's gonna be automatic), deleting
        return topDiv
    }

    override fun wrap() : String = "latext thing 1 $number $unit $element"
}