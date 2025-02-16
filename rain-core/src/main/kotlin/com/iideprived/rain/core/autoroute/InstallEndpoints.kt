package com.iideprived.rain.core.autoroute

import com.iideprived.rain.core.annotations.routing.httpmethod.*
import com.iideprived.rain.core.annotations.routing.httpmethod.HttpMethod
import com.iideprived.rain.core.annotations.routing.request.Body
import com.iideprived.rain.core.annotations.routing.request.Header
import com.iideprived.rain.core.annotations.routing.request.Path
import com.iideprived.rain.core.annotations.routing.request.RequestParameter
import com.iideprived.rain.core.exceptions.*
import com.iideprived.rain.core.model.response.GenericResponse
import com.iideprived.rain.core.util.returnsBaseResponse
import com.iideprived.rain.annotations.routing.httpmethod.*
import com.iideprived.rain.annotations.routing.httpmethod.HttpMethod
import com.iideprived.rain.annotations.routing.request.*
import com.iideprived.rain.exceptions.*
import com.iideprived.rain.model.response.BaseResponse
import com.iideprived.rain.model.response.ControlledResponse
import com.iideprived.rain.model.response.GenericResponse
import com.iideprived.rain.util.*
import io.github.classgraph.AnnotationInfo
import io.github.classgraph.ClassInfo
import io.github.classgraph.MethodInfo
import io.github.classgraph.MethodParameterInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import util.convertByString
import util.isNullable
import util.simpleType
import util.toKClass
import java.lang.reflect.InvocationTargetException


private fun AnnotationInfo.getValue(): String = this.parameterValues.firstOrNull()?.value.toString()

internal fun ClassInfo.installEndpoints(classLoader: ClassLoader, createInstance: (Class<*>) -> Any) : (Route.() -> Unit) = {
    val classInstance = createInstance(classLoader.loadClass(this@installEndpoints.name))
    declaredMethodInfo.forEach findingMethods@{ methodInfo ->
        val isControlled = methodInfo.returnsControlledResponse()
        val isBase = methodInfo.returnsBaseResponse()
        if (!isControlled && !isBase) {
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

            Connect::class.simpleName -> {
                route(annotationInfo.getValue(), io.ktor.http.HttpMethod("CONNECT")) {
                    handle(getRouteFunction(classInstance, methodInfo))
                }
            }
        }
    }
}

private fun getRouteFunction(classInstance: Any, methodInfo: MethodInfo) : (suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit ) = {
    val declaringClass = classInstance.javaClass
    val classLoader = declaringClass.classLoader
    val missingParameters: MutableList<MethodParameterInfo> = mutableListOf()
    val paramValues = methodInfo.parameterInfo.map { param ->
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
                Path::class.simpleName -> call.parameters[paramType.getValue()]?.convertByString(param.simpleType())
                Body::class.simpleName  -> call.receive(param.toKClass(classLoader))
                com.iideprived.rain.core.annotations.routing.request.Query::class.simpleName -> call.request.queryParameters[paramType.getValue()]?.convertByString(param.simpleType())
                Header::class.simpleName -> call.request.headers[paramType.getValue()]?.convertByString(param.simpleType())
                else -> throw RouteParameterAnnotationMissingException(methodInfo, param)
            }
        } catch (e: RouteParameterAnnotationMissingException){
            call.respond(com.iideprived.rain.core.model.response.BaseResponse.failure<GenericResponse>(e))
        }
        catch (e: Exception) {
            e.printStackTrace()
            call.respond(com.iideprived.rain.core.model.response.BaseResponse.failure<GenericResponse>(e))
        }

        if (!param.isNullable() && obj == null){
            missingParameters.add(param)
        }

        return@map obj
    }.toTypedArray()

    if (missingParameters.isNotEmpty()){
        call.respond(com.iideprived.rain.core.model.response.BaseResponse.failure<GenericResponse>(RouteParameterMissingException(methodInfo, missingParameters)))
    }

    var statusCode = 200
    val response = try {
            val method = declaringClass.getDeclaredMethod(
                methodInfo.name,
                *methodInfo.parameterInfo.map { it.toKClass(classLoader).java }.toTypedArray())

            var resultRaw = method.invoke(classInstance, *paramValues)
            val resultTyped: Any? = resultRaw
            if (resultTyped is BaseResponse){
                statusCode = resultTyped.statusCode
            } else if (resultTyped is ControlledResponse<*>){
                call.response.status(resultTyped.status)
                statusCode = resultTyped.status.value
                resultTyped.headers.forEach({ (k, v) ->
                    call.response.headers.append(k, v)
                })
                resultRaw = resultTyped.body
                call.response.headers.append(HttpHeaders.ContentType, resultTyped.contentType)
            }
            resultRaw
        } catch (invocationTargetException: InvocationTargetException){
            val e = invocationTargetException.targetException
            when {
                e is ErrorCodeException && e is StatusCodeException -> BaseResponse.failure<GenericResponse>(e, e.errorCode, e.statusCode)
                e is ErrorCodeException -> BaseResponse.failure<GenericResponse>(e, e.errorCode)
                e is StatusCodeException -> BaseResponse.failure<GenericResponse>(e, statusCode = e.statusCode)
                else -> BaseResponse.failure<GenericResponse>(e)
            }.apply { statusCode = this.statusCode }
        } catch (e: Exception){
            BaseResponse.failure<GenericResponse>(e).apply { statusCode = this.statusCode }
        }


    call.respond(HttpStatusCode.fromValue(statusCode), response)
}

