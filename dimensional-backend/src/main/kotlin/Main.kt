import com.google.gson.Gson
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.header
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.routing

data class Entry(val message: String)

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        routing {
            get("/api/ping/{count}") {
                println(call.parameters)
                var count = Integer.getInteger(call.parameters["count"] ?: "1") ?: 1
                println(count)
                val obj = Array<Entry>(count, {i -> Entry("yup")})
                val gson = Gson()
                val str = gson.toJson(obj)
                call.response.header("Access-Control-Allow-Origin", "*")
                call.respondText(str, ContentType.Application.Json)
            }
        }
    }.start(wait = true)
}