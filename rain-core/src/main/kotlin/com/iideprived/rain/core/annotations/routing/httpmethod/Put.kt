package com.iideprived.rain.core.annotations.routing.httpmethod

@HttpMethod
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Put(val path: String = "")