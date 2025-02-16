package com.iideprived.rain.implementation.jobs

import kotlin.reflect.full.createInstance

class RainSchedulerConfig {
    var scanPackages: List<String> = listOf()
    var createInstance: (Class<*>) -> Any = { it.kotlin.createInstance() }
    var classLoader: ClassLoader = this::class.java.classLoader
}