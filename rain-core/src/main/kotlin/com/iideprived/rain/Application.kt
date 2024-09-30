package com.iideprived.rain

import com.iideprived.rain.autoroute.Rain
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.ExperimentalSerializationApi
import util.isRainInitiated

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

@OptIn(ExperimentalSerializationApi::class)
internal fun Application.module() {
    isRainInitiated = true

    install(Rain)
}
