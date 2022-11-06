import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("io.ktor.plugin") version "2.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
    application
}

group = "org.example"
version = "1.1-SNAPSHOT"
val ktor_version = "2.1.3"
val kotlin_version = "1.7.20"
val exposed_version: String by project
val h2_version: String by project
val postgres_version: String by project

repositories {
    mavenCentral()
    /* This was the missing piece for finding build/install/.../bin/... script */
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
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

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("org.postgresql:postgresql:$postgres_version")

    /* Test dependencies */
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

application {
    mainClass.set("ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

// Alias "installDist" as "stage" (for cloud providers)
tasks.create("stage") {
    dependsOn(tasks.getByName("installDist"))
}