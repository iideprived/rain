
val skipPublishing = listOf("rain-shared")
plugins {
    id("maven-publish")
    alias(libs.plugins.kotlin.jvm)
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/iideprived/p/rain/maven")
        maven(url = "https://jitpack.io")
    }
}

subprojects {

    group = "com.iideprived.rain"
    version = "1.4.12"

    installJvmModule(project)
    installKtorBase(project)
    installPublishing(project)
}

fun installKtorBase(project: Project) {
    project.plugins.withId("io.ktor.plugin"){
        project.dependencies {
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cors)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.server.content.negotiation)
            testImplementation(libs.ktor.server.test.host)
        }
    }
}

fun installJvmModule(project: Project) {
    project.plugins.withId("org.jetbrains.kotlin.jvm") {
        project.dependencies {
            if (project.name != "rain-shared") {
                implementation(project(":rain-shared"))
            }

            testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.8.21")
            testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
            testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.3")
            testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
        }
        project.tasks.test {
            useJUnitPlatform()
        }

        if (project.name in skipPublishing) {
            java {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(17))
                }
            }

            tasks.jar {
                archiveBaseName.set("${project.name}-$version")
            }

            configurations {
                all {
                    attributes.attribute(
                        org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.attribute,
                        org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.jvm
                    )
                }
            }
        }
    }
}

fun installPublishing(project: Project) {

    project.plugins.withId("maven-publish") {
        apply(plugin = "org.jetbrains.kotlin.jvm")

        project.tasks.named("publishToMavenLocal") {
            dependsOn(project.tasks.named("assemble"))
        }

        java {
            withSourcesJar()
            withJavadocJar()
        }

        project.publishing {
            publications {
                create<MavenPublication>("mavenJava") {
                    from(project.components["kotlin"])
                    groupId = project.group.toString()
                    artifactId = project.name
                    version = project.version.toString()
                }
//                create<MavenPublication>("gpr") {
//                    groupId = "com.github.iideprived"
//                    artifactId = project.name
//                    version = project.version.toString()
//
//                    from(project.components["kotlin"])
//                }
            }
            if (project.name in skipPublishing){
                repositories {
                    mavenLocal()
                }
            } else {
//                repositories {
//                    maven {
//                        val user = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USER")
//                        val key = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_PUBLISHING_TOKEN")
//                        name = "GitHubPackages"
//                        url = uri("https://maven.pkg.github.com/$user/${project.name}")
//                        credentials {
//                            username = user
//                            password = key
//                        }
//                    }
//                }
            }
        }
    }
}