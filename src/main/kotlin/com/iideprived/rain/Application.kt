package com.iideprived.rain

import com.iideprived.rain.implementation.autoroute.installServiceAnnotatedRoutes
import io.ktor.server.application.*

internal fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

internal fun Application.module() {
    installServiceAnnotatedRoutes()
}