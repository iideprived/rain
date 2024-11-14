package com.iideprived.rain.core.routes

import com.iideprived.rain.core.annotations.routing.Service
import com.iideprived.rain.core.annotations.routing.httpmethod.Get
import com.iideprived.rain.core.annotations.routing.httpmethod.Post
import com.iideprived.rain.core.annotations.routing.request.Body
import com.iideprived.rain.core.model.request.ErrorRequest
import com.iideprived.rain.core.model.response.GenericResponse

@Service("/error")
internal class ErrorRouter {

    @Get
    internal fun getError(): GenericResponse = throw com.iideprived.rain.core.exceptions.GenericException()

    @Get("/code")
    internal fun getErrorCode(): GenericResponse = throw com.iideprived.rain.core.exceptions.ErrorTestException()

    @Get("/status")
    internal fun getErrorStatus(): GenericResponse = throw com.iideprived.rain.core.exceptions.StatusTestException()

    @Get("/codeAndStatus")
    internal fun getErrorCodeAndStatus(): GenericResponse = throw com.iideprived.rain.core.exceptions.ErrorStatusTestException()

    @Post
    internal fun postError(@Body request: ErrorRequest) : GenericResponse = when(request) {
        ErrorRequest(hasStatus = true, hasCode = false) -> throw com.iideprived.rain.core.exceptions.StatusTestException()
        ErrorRequest(hasStatus = false, hasCode = true) -> throw com.iideprived.rain.core.exceptions.ErrorTestException()
        ErrorRequest(hasStatus = true, hasCode = true) -> throw com.iideprived.rain.core.exceptions.ErrorStatusTestException()
        else -> throw com.iideprived.rain.core.exceptions.GenericException()
    }
}