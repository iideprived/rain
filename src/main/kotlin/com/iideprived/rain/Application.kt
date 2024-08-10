package com.iideprived.rain

import com.iideprived.rain.implementation.autoroute.installServiceAnnotatedRoutes
import io.ktor.server.application.*
import kotlinx.serialization.ExperimentalSerializationApi

internal fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@OptIn(ExperimentalSerializationApi::class)
internal fun Application.module() {
    installServiceAnnotatedRoutes(){
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }
}

