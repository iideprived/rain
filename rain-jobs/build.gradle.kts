
plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":rain-core"))
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlin.reflect)
    implementation(libs.classgraph)
}

application {
    mainClass = "com.iideprived.rain.jobs.MainKt"
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}