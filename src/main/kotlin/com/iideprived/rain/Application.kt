package com.iideprived.rain

import com.iideprived.rain.implementation.autoroute.installServiceAnnotatedRoutes
import com.iideprived.rain.implementation.autoroute.isRainInitiated
import io.ktor.server.application.*
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
internal fun Application.module() {
    isRainInitiated = true
    installServiceAnnotatedRoutes()
}
