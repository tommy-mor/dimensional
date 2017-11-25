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
//TODO clean up code ALOT
//TODO add canvas2image lib supoprt for KaTex
//TODO work on wrap function to make into KaTex fomula
//TODO FIX any bugs
//TODO smooth out ui experience and make it look nice
//TODO rewrite entire thing to make it not really trash. maybe find a kotlin ui frameowork - cont -
// but DEF separate concerns more and make it recursive nice style like my interpreter
//TODO auto cancelling units
//TODO fix backwards tab, tab outside of textarea, tab inital thing is not number
//TODO make site look nice, add explanation, be a hot designer and make it look boujee
//TODO fix formatting for elements with subscripts
//TODO fix "1" mode and fix the last elementa adding when ur tryna finish (add a better non-rendering plus thing)

//mode codes - crossed, normal, one, equals
//mode codes - x, n, 1, =

var tableList = mutableListOf(Plate(Segment(),Segment()))
var table = Table(tableList)

external class katex {
    companion object {
        fun render(code: String, parent: HTMLElement)
    }
}

fun main(args: Array<String>) {

    val renderDiv = document.create.div("output") {+"thisdiv" }
    document.body!!.appendChild(renderDiv)
    window.onload = {
        document.body!!.append(table.render(document.body!!, document))
    }
    println(table.wrap())
    window.setInterval({katex.render(table.wrap(),renderDiv)},1000)
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

    override fun wrap() : String = "${plates.joinToString(" ") { it.wrap() }}"
    override  fun render(parent : Element, document : Document) : HTMLElement  {
        div.id = "tableId"
        plates.forEach { div.appendChild(it.render(div, document)) }
        return div
    }
}

class Plate(val top : Segment, val bottom : Segment) : Renderable {

    override fun wrap() : String  {
        //TODO fix this egregious hack
        var topString = top.wrap()
        var prep = ""
        if(top.mode == "=") {
            topString = topString.drop(1)
            prep = "="
        }
        return "$prep \\frac{$topString}{${bottom.wrap()}}"
    }

    override fun render(parent: Element, document: Document) : HTMLElement {
        val div = document.create.div("plate") { + "" }
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

//these are not all mutually exclusive, <- code smell
enum class State {
    VIEW, EDIT, NEW, ONE, EQUALS
}


class Segment (var number : Double = 0.0, var unit : String = "Unit", var element : String = "Element", var mode : String="n") : Renderable {
    var state : State = State.NEW
    var focusState = 0

    fun updateOnString(input : String) {
        if (mode == "=") state = State.EQUALS
        if (mode == "1") state = State.ONE; else {
            val all = input.split(",")
            println(all)
            this.number = all[0].toDoubleOrNull() ?: throw IllegalArgumentException("not a double")
            this.unit = all[1]
            this.element = all[2]
            this.mode = all[3].trim()
        }
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
                    onKeyDownFunction = {event ->

                        val editbox = parent.querySelector("#editbox")!! as HTMLTextAreaElement
                        if(editbox.value.split(",").size < 4) editbox.value += ','
                        event as KeyboardEvent
                        if (event.keyCode == 9) {
                            event.preventDefault()
                            val box = event.target as HTMLTextAreaElement
                            val a = box.value.split(",")

                            box.selectionStart = a.take(focusState).map { it.length }.sum() + focusState
                            box.selectionEnd = a.take(focusState).map { it.length }.sum() + focusState + a[focusState].length

                            focusState++
                            focusState = focusState % 4
                        }
                    }

                    onKeyPressFunction = {event ->
                        event as KeyboardEvent
                        //is 0 on mac, 13 on linux. TODO test windows/find real solution
                        val editbox = parent.querySelector("#editbox")!! as HTMLTextAreaElement

                        if (event.charCode == 0 || event.charCode == 13) {
                            state = State.VIEW
                            updateOnString(editbox.value)
                            topDiv.removeChild(editbox)
                            textDiv.innerHTML = if(state == State.ONE) "-------" else "$number $unit $element"
                            topDiv.appendChild(textDiv)
                        }
                        if(event.keyCode == 9) {
                            event.preventDefault()
                            this as HTMLTextAreaElement
                            this.selectionStart = 0
                        }
                    }
                    + "$number, $unit, $element, $mode"
                })
            }
        })

        //TODO add button for toggling crossed out status (temp cause it's gonna be automatic), deleting
        return topDiv
    }

    //TODO, element should not be wrap maybe cause it can't do subscripts, but non-text has bad font ehh
    override fun wrap() : String {
        if(state == State.ONE) {
            return "1"
        }
        if (mode == "x") {
            return "$number \\text{ \\ } \\cancel{$unit} \\text{\\  $element}"
        } else if (mode == "="){
            return "= $number \\text{ \\ } $unit \\text{\\  $element}"
        } else{
            return "$number \\text{ \\ } $unit \\text{\\  $element}"
        }
    }
}


//document.body!!.querySelector("#demobox")!!.innerHTML = table.wrap()

//todo -> integrate with KaTeX and canvas2image