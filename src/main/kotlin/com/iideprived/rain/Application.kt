package com.iideprived.rain

import com.iideprived.di.appModule
import com.iideprived.di.routerModule
import com.iideprived.di.serviceModule
import com.iideprived.rain.implementation.autoroute.installServiceAnnotatedRoutes
import io.ktor.server.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.ktor.ext.getKoin
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

internal fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@OptIn(ExperimentalSerializationApi::class)
internal fun Application.module() {
    install(Koin){
        slf4jLogger()
        modules(
            appModule,
            serviceModule,
            routerModule,
        )
    }

    installServiceAnnotatedRoutes(
        createInstance = {
            getKoin().get(loadClass().kotlin)
        }
    )
}
