plugins {
    kotlin("jvm") version "2.3.20"
    id("org.jetbrains.compose") version "1.8.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.20"
}

group = "io.github.conno2429.chehh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    testImplementation(kotlin("test"))
}

compose.desktop {
    application {
        mainClass = "io.github.conno2429.chehh.ChehhKt"
    }
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}