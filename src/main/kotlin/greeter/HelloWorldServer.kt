package greeter

import io.grpc.Server
import io.grpc.ServerBuilder

class HelloWorldServer(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(HelloWorldService())
        .build()

    fun start() {
        server.start()
        println("Server running on port $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("**** Shutting down gRPC server since JVM is shutting down")
                server.shutdown()
                println("**** Server shut down")
            }
        )
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    class HelloWorldService: GreeterGrpcKt.GreeterCoroutineImplBase() {
        override suspend fun sayHello(request: HelloRequest) = helloReply {
            message = "Hello, ${request.name}"
        }

    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = HelloWorldServer(port)
    server.start()
    server.blockUntilShutdown()
}