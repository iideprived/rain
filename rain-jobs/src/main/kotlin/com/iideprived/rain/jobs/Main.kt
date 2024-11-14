package com.iideprived.rain.jobs

import com.iideprived.rain.jobs.implementation.jobs.RainScheduler
import io.ktor.server.application.*
import util.isRainInitiated

internal fun Application.module(){
    isRainInitiated = true
    install(RainScheduler)
}