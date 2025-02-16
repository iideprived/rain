package com.iideprived.rain.core.annotations.routing

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Service(val path: String = "")
