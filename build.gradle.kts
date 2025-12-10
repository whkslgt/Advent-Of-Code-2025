plugins {
    kotlin("jvm") version "2.2.20"
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.masabi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.ortools:ortools-java:9.14.6206")
    runtimeOnly("com.google.ortools:ortools-darwin-aarch64:9.14.6206")
}

kotlin {
    jvmToolchain(21)
}

jmh {
    warmupIterations = 3
    iterations = 5
    fork = 2
}