package com.iideprived.rain.implementation.autoroute

import com.iideprived.rain.annotations.*
import com.iideprived.rain.exceptions.*
import com.iideprived.rain.model.response.BaseResponse
import com.iideprived.rain.util.*
import io.github.classgraph.AnnotationInfo
import io.github.classgraph.ClassInfo
import io.github.classgraph.MethodInfo
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlin.reflect.full.createInstance

@OptIn(ExperimentalSerializationApi::class)
fun Application.installServiceAnnotatedRoutes(jsonBuilder: JsonBuilder.() -> Unit = {
    prettyPrint = false
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
}) {
    install(ContentNegotiation){
        json(Json {
            jsonBuilder.invoke(this)
        })
    }
    routing {
        scanResult.getClassesWithAnnotation(Service::class.qualifiedName).forEach { classInfo ->
            val servicePath = classInfo.annotationInfo.firstOrNull()?.parameterValues?.firstOrNull()?.value.toString()
            route(servicePath, classInfo.installEndpoints())
        }
    }
}

private fun AnnotationInfo.getValue(): String = this.parameterValues.firstOrNull()?.value.toString()

private fun ClassInfo.installEndpoints() : (Route.() -> Unit) = {
    val classInstance = loadClass().kotlin.createInstance()
    methodInfo.forEach findingMethods@{ methodInfo ->
        if (!methodInfo.returnsBaseResponse()) {
            throw RouteMethodReturnTypeException(methodInfo)
        }

        val annotationInfos = methodInfo.annotationInfo.filter { annotation ->
            annotation.classInfo.annotations.any { it.name == HttpMethod::class.qualifiedName }
        }
        if (annotationInfos.isEmpty()) return@findingMethods
        if (annotationInfos.size > 1) {
            throw RouteMethodMultipleHttpMethodAnnotationException(methodInfo)
        }

        val annotationInfo = annotationInfos.first()

        when (annotationInfo.classInfo.simpleName) {
            Get::class.simpleName -> {
                get(annotationInfo.getValue(), getRouteFunction(classInstance, methodInfo))
            }
            Post::class.simpleName -> {
                post(annotationInfo.getValue(), getRouteFunction(classInstance, methodInfo))
            }
            Put::class.simpleName -> {
                put(annotationInfo.getValue(), getRouteFunction(classInstance, methodInfo))
            }
            Delete::class.simpleName -> {
                delete(annotationInfo.getValue(), getRouteFunction(classInstance, methodInfo))
            }
            Patch::class.simpleName -> {
                patch(annotationInfo.getValue(), getRouteFunction(classInstance, methodInfo))
            }
            Options::class.simpleName -> {
                options(annotationInfo.getValue(), getRouteFunction(classInstance, methodInfo))
            }
            Head::class.simpleName -> {
                head(annotationInfo.getValue(), getRouteFunction(classInstance, methodInfo))
            }
        }
    }
}

private fun getRouteFunction(classInstance: Any, methodInfo: MethodInfo) : (suspend RoutingContext.() -> Unit ) = {
    val paramValues = methodInfo.parameterInfo.mapNotNull { param ->
        val paramAnnotations = param.annotationInfo.filter { paramAnnotation ->
            paramAnnotation.classInfo.annotations.any { it.name == RequestParameter::class.qualifiedName }
        }
        if (paramAnnotations.size > 1){
            throw RouteParameterMultipleAnnotationException(methodInfo, param)
        }
        if (paramAnnotations.isEmpty()) {
            throw RouteParameterAnnotationMissingException(methodInfo, param)
        }

        val paramType = paramAnnotations.first()

        var obj: Any? = null
        try {
            obj = when (paramType.classInfo?.simpleName) {
                Path::class.simpleName -> call.pathParameters[paramType.getValue()]?.convertByString(param.simpleType())
                Body::class.simpleName -> call.receive(param.toKClass())
                Query::class.simpleName -> call.queryParameters[paramType.getValue()]?.convertByString(param.simpleType())
                Header::class.simpleName -> call.request.headers[paramType.getValue()]?.convertByString(param.simpleType())
                else -> throw RouteParameterAnnotationMissingException(methodInfo, param)
            }
        } catch (e: RouteParameterAnnotationMissingException){
            call.respond(BaseResponse.failure(e))
        }
        catch (e: Exception){
            e.printStackTrace()
            call.respond(BaseResponse.failure(e))
        }

        return@mapNotNull obj
    }.toTypedArray()

    if (paramValues.size != methodInfo.parameterInfo.size){
        call.respond(BaseResponse.failure(Exception("Failed to parse parameters")))
    } else {
        try {
            call.respond(methodInfo.loadClassAndGetMethod().invoke(classInstance, *paramValues))
        } catch (e: Exception){
            call.respond(when {
                e is ErrorCodeException && e is StatusCodeException -> BaseResponse.failure(e, e.errorCode, e.statusCode)
                e is ErrorCodeException -> BaseResponse.failure(e, e.errorCode)
                e is StatusCodeException -> BaseResponse.failure(e, statusCode = e.statusCode)
                else -> BaseResponse.failure(e)
            })
        }
    }
}