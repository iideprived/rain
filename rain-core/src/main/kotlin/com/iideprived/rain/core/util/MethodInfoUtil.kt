package com.iideprived.rain.core.util

import com.iideprived.rain.core.model.response.BaseResponse
import io.github.classgraph.MethodInfo
import util.implementsInterface
import util.returnType

internal fun MethodInfo.returnsBaseResponse() : Boolean = returnType().toString().implementsInterface(BaseResponse::class) || returnType().toString() == BaseResponse::class.qualifiedName
