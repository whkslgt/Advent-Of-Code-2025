plugins {
    kotlin("jvm") version "2.2.20"
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.masabi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

jmh {
    warmupIterations = 3
    iterations = 5
    fork = 2
}