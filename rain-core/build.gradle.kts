
plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":rain-shared"))
    implementation(libs.kotlin.reflect)
    implementation(libs.classgraph)
}