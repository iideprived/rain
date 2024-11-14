package com.iideprived.rain.core

import com.iideprived.rain.core.autoroute.Rain
import io.ktor.server.application.*
import util.isRainInitiated

internal fun Application.module() {
    isRainInitiated = true
    install(Rain)
}