plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
//    `maven-publish`
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.classgraph)

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
