package greeter

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

class HelloWorldClient(channel: ManagedChannel) {
    private val stub = GreeterGrpcKt.GreeterCoroutineStub(channel)

    suspend fun greet(name: String) {
        println("Sending request for \"$name\"")
//        val request = HelloRequest.newBuilder().setName(name).build()
        val request = helloRequest { this.name = name }
        val response = stub.sayHello(request)
        println("Received response: ${response.message}")
    }
}

suspend fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
    val client = HelloWorldClient(channel)
    client.greet("Zisulin")

}