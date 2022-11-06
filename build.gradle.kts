import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"
val ktor_version = "2.1.3"
val kotlin_version = "1.7.20"

repositories {
    mavenCentral()
}

dependencies {
    /* Ktor */
    implementation("io.ktor:ktor-server-core:2.1.3") {
        because("this is for the Ktor core ")
    }
    implementation("io.ktor:ktor-server-netty:2.1.3")

    implementation("ch.qos.logback:logback-classic:1.4.4") {
        because("This is to remove log error for scaffold")
    }

    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation("io.ktor:ktor-server-cors:$ktor_version")

    /* Test dependencies */
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ApplicationKt")
}