rootProject.name = "com.iideprived.rain"

include("rain-core", "rain-jobs", "rain-shared")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "2.0.10")
            version("ktor", "2.3.0")
            version("koin", "4.0.0-RC1")
            version("coroutines", "1.7.3")
            version("logback", "1.4.12")
            version("classgraph", "4.8.162")
            version("junit", "5.9.3")

            library("logback.classic", "ch.qos.logback", "logback-classic").versionRef("logback")

            library("ktor.server.core", "io.ktor", "ktor-server-core-jvm").versionRef("ktor")
            library("ktor.server.cors", "io.ktor", "ktor-server-cors-jvm").versionRef("ktor")
            library("ktor.server.netty", "io.ktor", "ktor-server-netty-jvm").versionRef("ktor")
            library("ktor.server.config.yaml", "io.ktor", "ktor-server-config-yaml").versionRef("ktor")
            library("ktor.server.resources", "io.ktor", "ktor-server-resources-jvm").versionRef("ktor")
            library("ktor.serialization.kotlinx.json", "io.ktor", "ktor-serialization-kotlinx-json-jvm").versionRef("ktor")
            library("ktor.server.content.negotiation", "io.ktor", "ktor-server-content-negotiation-jvm").versionRef("ktor")

            library("kotlin.reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin")
            library("classgraph", "io.github.classgraph", "classgraph").versionRef("classgraph")
            library("coroutines.core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("coroutines")
            library("ktor.server.test.host", "io.ktor", "ktor-server-test-host-jvm").versionRef("ktor")
            library("kotlin.test.junit5", "org.jetbrains.kotlin", "kotlin-test-junit5").version("1.8.21")
            library("junit.jupiter.api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit.jupiter.params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            library("junit.jupiter.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("koin.core", "io.insert-koin", "koin-core").versionRef("koin")

            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("ktor", "io.ktor.plugin").versionRef("ktor")
            plugin("kotlin-serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
        }
    }
}
