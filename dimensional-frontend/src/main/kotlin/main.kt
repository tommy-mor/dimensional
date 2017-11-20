import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    window.onload = {
        fetch("thing")
        val input = document.getElementById("count_id") as HTMLInputElement
        val button = document.getElementById("button_id")
        button?.addEventListener("click", fun(event: Event) {
            println(input.value)
            fetch(input.value)
        })
    }
}

fun fetch(count : String) {
    var url = "https://localhost:8080/api/ping/$count"
    val req = XMLHttpRequest()
}