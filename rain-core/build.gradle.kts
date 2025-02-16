
plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":rain-shared"))
    implementation(libs.kotlin.reflect)
    implementation(libs.classgraph)
}

application {
    mainClass = "com.iideprived.rain.core.MainKt"
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}