# Minimal Kotlin gRPC
A most minimal Gradle project setup for using gRPC with Kotlin.

Being new to the Kotlin/Gradle ecosystem, I struggled with creating a bare-bones project setup to be able to experiment around. This is a minimal configuration to play around with.

## Usage
There are two custom tasks to run the server and client respectively:
```shell
./gradlew runServer
```
And in another console:
```shell
./gradlew runClient
```

You can use a tool like [`grpcui`](https://github.com/fullstorydev/grpcui) to interact with the gRPC server.
For the server as its configured here, use the following command:
```shell
 grpcui -plaintext -proto src/main/proto/greeter.proto localhost:50051
```

## Prior Art
I found [this Reddit thread](https://www.reddit.com/r/Kotlin/comments/18y2ei1/where_to_find_good_tutorials_kotlin_grpc_gradle/) that described the exact same problem that I had. axymthr's [kotlin-samples](https://github.com/axymthr/kotlin-samples) repo from the comments helped me a lot, although it was still too complex for what I wanted.