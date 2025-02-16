package com.iideprived.rain.core.routes

import com.iideprived.rain.core.annotations.routing.Service
import com.iideprived.rain.core.annotations.routing.httpmethod.Get
import com.iideprived.rain.core.annotations.routing.httpmethod.Post
import com.iideprived.rain.core.annotations.routing.request.Body
import com.iideprived.rain.core.exceptions.ErrorStatusTestException
import com.iideprived.rain.core.exceptions.ErrorTestException
import com.iideprived.rain.core.exceptions.GenericException
import com.iideprived.rain.core.exceptions.StatusTestException
import com.iideprived.rain.core.model.request.ErrorRequest
import com.iideprived.rain.core.model.response.GenericResponse

@Service("/error")
internal class ErrorRouter {

    @Get
    internal fun getError(): GenericResponse = throw GenericException()

    @Get("/code")
    internal fun getErrorCode(): GenericResponse = throw ErrorTestException()

    @Get("/status")
    internal fun getErrorStatus(): GenericResponse = throw StatusTestException()

    @Get("/codeAndStatus")
    internal fun getErrorCodeAndStatus(): GenericResponse = throw ErrorStatusTestException()

    @Post
    internal fun postError(@Body request: ErrorRequest) : GenericResponse = when(request) {
        ErrorRequest(hasStatus = true, hasCode = false) -> throw StatusTestException()
        ErrorRequest(hasStatus = false, hasCode = true) -> throw ErrorTestException()
        ErrorRequest(hasStatus = true, hasCode = true) -> throw ErrorStatusTestException()
        else -> throw GenericException()
    }
}