// Build script taken from:
// https://www.reddit.com/r/Kotlin/comments/18y2ei1/where_to_find_good_tutorials_kotlin_grpc_gradle/
import com.google.protobuf.gradle.id

buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    idea
    java
    application
    kotlin("jvm") version "1.8.0"
    id("com.google.protobuf") version "0.9.1"
}

group = "org.morlinbrot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    val grpc = create("grpc") {
    }
    getByName("test") {
        compileClasspath += grpc.output
        runtimeClasspath += grpc.output
    }
}

val grpcVersion = "1.51.0"
val grpcKotlinVersion = "1.3.0"
val protobufVersion = "3.21.12"
val coroutinesVersion = "1.6.4"
val mainClassPath = "greeter.HelloWorldServerKt"

dependencies {
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")
    runtimeOnly("io.grpc:grpc-netty-shaded:$grpcVersion")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = mainClassPath
}

kotlin {
    jvmToolchain(17)
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        // Optional: an artifact spec for a protoc plugin, with "grpc" as
        // the identifier, which can be referred to in the "plugins"
        // container of the "generateProtoTasks" closure.
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }
    // omitted protoc and plugins config
    generateProtoTasks {
        all().forEach {
            // omitted plugins config
            it.builtins {
                id("kotlin")
            }
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without
                // options. Note the braces cannot be omitted, otherwise the
                // plugin will not be added. This is because of the implicit way
                // NamedDomainObjectContainer binds the methods.
                id("grpc") { }
                id("grpckt") { }
            }
        }
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17" // Java 8 compatibility
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "17" // Java 8 compatibility for test Kotlin code
    }

    // Define a custom run task
    register("buildAndRun", JavaExec::class) {
        group = "execution"
        description = "Runs the application after generating proto files and compiling Kotlin code"
        mainClass = mainClassPath

        classpath = sourceSets["main"].runtimeClasspath
        dependsOn("generateProto", "compileKotlin")
    }
}

