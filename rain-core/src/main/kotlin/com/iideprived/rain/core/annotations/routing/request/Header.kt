package com.iideprived.rain.core.annotations.routing.request

@RequestParameter
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Header(val name: String)
