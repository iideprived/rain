package com.iideprived.rain.autoroute

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonBuilder
import kotlin.reflect.full.createInstance

class RainConfig {
    var routePrefix: String = ""
    var scanPackages: List<String> = listOf()

    @OptIn(ExperimentalSerializationApi::class)
    var jsonBuilder: JsonBuilder.() -> Unit = {
        prettyPrint = false
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }

    var createInstance: (Class<*>) -> Any = { it.kotlin.createInstance() }
    var classLoader: ClassLoader = this::class.java.classLoader
}