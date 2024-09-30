package com.iideprived.rain.exceptions

import com.iideprived.rain.annotations.routing.httpmethod.HttpMethod
import com.iideprived.rain.annotations.routing.request.*
import util.simpleType
import io.github.classgraph.MethodInfo
import io.github.classgraph.MethodParameterInfo

internal open class RouteInstallationException : Exception() {
    override val message: String
        get() = "Failed to install route"
}

internal class RouteMethodReturnTypeException(private val methodInfo: MethodInfo) : RouteInstallationException() {
    override val message: String
        get() = "${methodInfo.classInfo.simpleName}.${methodInfo.name} " +
                "must return a subclass of BaseResponse. Instead it returns " +
                "${methodInfo.typeSignatureOrTypeDescriptor.resultType}"
}

internal class RouteParameterAnnotationMissingException(private val methodInfo: MethodInfo, private val paramInfo: MethodParameterInfo) : RouteInstallationException() {
    override val message: String
        get() = "${methodInfo.classInfo.simpleName}.${methodInfo.name} " +
                " parameter ${paramInfo.name} must be annotated with " +
                "@${Path::class.simpleName}, @${Body::class.simpleName}, @${Query::class.simpleName}, or @${Header::class.simpleName}"
}

internal class RouteParameterMultipleAnnotationException(private val methodInfo: MethodInfo, private val paramInfo: MethodParameterInfo) : RouteInstallationException() {
    private val requestParameterAnnotations = paramInfo.annotationInfo.filter { paramAnnotation ->
        paramAnnotation.classInfo.annotations.any { it.name == RequestParameter::class.qualifiedName }
    }.map { "@${it.classInfo.simpleName}" }
    override val message: String
        get() = "${methodInfo.classInfo.simpleName}.${methodInfo.name} " +
                " parameter ${paramInfo.name} cannot have multiple ${RequestParameter::class.simpleName} annotations. It has [$requestParameterAnnotations]"
}

internal class RouteMethodMultipleHttpMethodAnnotationException(private val methodInfo: MethodInfo) : RouteInstallationException() {
    private val httpMethodAnnotations = methodInfo.annotationInfo.filter { annotation ->
        annotation.classInfo.annotations.any { it.name == HttpMethod::class.qualifiedName }
    }.map { "@${it.classInfo.simpleName}" }
    override val message: String
        get() = "${methodInfo.classInfo.simpleName}.${methodInfo.name} " +
                " cannot have multiple ${HttpMethod::class.simpleName} annotations. It has [$httpMethodAnnotations]"
}

internal class RouteParameterMissingException(private val methodInfo: MethodInfo, private val missing: List<MethodParameterInfo>) : RouteInstallationException() {
    override val message: String
        get() = "${methodInfo.classInfo.simpleName}.${methodInfo.name} " +
                " is missing parameters: ${missing.joinToString { "${it.name}: ${it.simpleType()}" }}"
}