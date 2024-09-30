package com.iideprived.rain.autoroute

import com.iideprived.rain.annotations.routing.Service
import util.getScanResult
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import util.isRainInitiated

val Rain = createApplicationPlugin("Rain", createConfiguration = ::RainConfig){
    val pluginConfig = pluginConfig
    application.install(ContentNegotiation){
        json(Json {
            pluginConfig.jsonBuilder.invoke(this)
        })
    }

    application.routing {
        getScanResult(pluginConfig.classLoader).getClassesWithAnnotation(Service::class.qualifiedName)
            .filter { classInfo -> isRainInitiated || !classInfo.packageName.startsWith("com.iideprived.rain")  }
            .filter { classInfo -> pluginConfig.scanPackages.isEmpty() || pluginConfig.scanPackages.any { classInfo.packageName.startsWith(it) } }
            .forEach { classInfo ->
                val servicePath = classInfo.annotationInfo.firstOrNull()?.parameterValues?.firstOrNull()?.value.toString()
                route(pluginConfig.routePrefix + servicePath, classInfo.installEndpoints(pluginConfig.classLoader, pluginConfig.createInstance))
            }
    }
}