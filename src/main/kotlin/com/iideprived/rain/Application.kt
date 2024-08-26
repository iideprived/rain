package com.iideprived.rain

import com.iideprived.rain.implementation.autoroute.installServiceAnnotatedRoutes
import io.ktor.server.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.reflect.full.createInstance

internal fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@OptIn(ExperimentalSerializationApi::class)
internal fun Application.module() {
    installServiceAnnotatedRoutes(
        jsonBuilder = {
            prettyPrint = false
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
        },
        createInstance = {
            loadClass().kotlin.createInstance()
        }
    )
}
