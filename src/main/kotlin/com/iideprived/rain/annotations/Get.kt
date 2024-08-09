package com.iideprived.rain.annotations

@HttpMethod
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Get(val path: String = "")
