package com.iideprived.rain.util

import com.iideprived.rain.model.response.BaseResponse
import com.iideprived.rain.model.response.ControlledResponse
import com.iideprived.rain.model.response.JsonResponse
import io.github.classgraph.MethodInfo
import util.implementsInterface
import util.returnType

internal fun MethodInfo.returnsControlledResponse() : Boolean {
    val isSubclass = returnType().toString().implementsInterface(ControlledResponse::class)
    val isControlledResponse = returnType().toString() == ControlledResponse::class.qualifiedName
    val isJsonResponse = returnType().toString().contains(JsonResponse::class.simpleName.toString())
    return isSubclass || isControlledResponse || isJsonResponse
}
internal fun MethodInfo.returnsBaseResponse() : Boolean = returnType().toString().implementsInterface(BaseResponse::class) || returnType().toString() == BaseResponse::class.qualifiedName

