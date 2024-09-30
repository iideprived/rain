package com.iideprived.rain.annotations.routing.request

@RequestParameter
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Query(val name: String)
