package com.iideprived.rain.annotations.routing.httpmethod

@HttpMethod
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Connect(val path: String = "")