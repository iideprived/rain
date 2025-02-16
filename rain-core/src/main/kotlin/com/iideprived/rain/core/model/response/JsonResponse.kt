package com.iideprived.rain.core.model.response

import io.ktor.http.*
import kotlinx.serialization.Serializable

data class JsonResponse<T : BaseResponse>(
    @Serializable val data: T,
    val exception: Exception? = null,
    override val headers: Map<String, String> = emptyMap(),
) : ControlledResponse<T>(
    data,
    emptyMap(),
    contentType = "application/json",
    status = HttpStatusCode.fromValue(data.statusCode))

