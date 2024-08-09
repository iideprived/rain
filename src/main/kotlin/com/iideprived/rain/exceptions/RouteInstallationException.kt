package com.iideprived.rain.exceptions

import com.iideprived.rain.annotations.*
import com.iideprived.rain.annotations.Body
import io.github.classgraph.MethodInfo
import io.github.classgraph.MethodParameterInfo

open class RouteInstallationException : Exception() {
    override val message: String
        get() = "Failed to install route"
}

class RouteMethodReturnTypeException(private val methodInfo: MethodInfo) : RouteInstallationException() {
    override val message: String
        get() = "${methodInfo.classInfo.simpleName}.${methodInfo.name} " +
                "must return a subclass of BaseResponse. Instead it returns " +
                "${methodInfo.typeSignatureOrTypeDescriptor.resultType}"
}

class RouteParameterAnnotationMissingException(private val methodInfo: MethodInfo, private val paramInfo: MethodParameterInfo) : RouteInstallationException() {
    override val message: String
        get() = "${methodInfo.classInfo.simpleName}.${methodInfo.name} " +
                " parameter ${paramInfo.name} must be annotated with " +
                "@${Path::class.simpleName}, @${Body::class.simpleName}, @${Query::class.simpleName}, or @${Header::class.simpleName}"
}

class RouteParameterMultipleAnnotationException(private val methodInfo: MethodInfo, private val paramInfo: MethodParameterInfo) : RouteInstallationException() {
    private val requestParameterAnnotations = paramInfo.annotationInfo.filter { paramAnnotation ->
        paramAnnotation.classInfo.annotations.any { it.name == RequestParameter::class.qualifiedName }
    }.map { "@${it.classInfo.simpleName}" }
    override val message: String
        get() = "${methodInfo.classInfo.simpleName}.${methodInfo.name} " +
                " parameter ${paramInfo.name} cannot have multiple ${RequestParameter::class.simpleName} annotations. It has [$requestParameterAnnotations]"
}

class RouteMethodMultipleHttpMethodAnnotationException(private val methodInfo: MethodInfo) : RouteInstallationException() {
    private val httpMethodAnnotations = methodInfo.annotationInfo.filter { annotation ->
        annotation.classInfo.annotations.any { it.name == HttpMethod::class.qualifiedName }
    }.map { "@${it.classInfo.simpleName}" }
    override val message: String
        get() = "${methodInfo.classInfo.simpleName}.${methodInfo.name} " +
                " cannot have multiple ${HttpMethod::class.simpleName} annotations. It has [$httpMethodAnnotations]"
}