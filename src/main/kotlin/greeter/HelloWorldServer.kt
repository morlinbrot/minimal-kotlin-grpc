package greeter

class HelloWorldServer(val port: Int) {
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = HelloWorldServer(port)
    println("Server *not* running.")
}