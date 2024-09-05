package com.iideprived.rain.routes

import com.iideprived.rain.annotations.Body
import com.iideprived.rain.annotations.Get
import com.iideprived.rain.annotations.Post
import com.iideprived.rain.annotations.Service
import com.iideprived.rain.exceptions.ErrorStatusTestException
import com.iideprived.rain.exceptions.ErrorTestException
import com.iideprived.rain.exceptions.GenericException
import com.iideprived.rain.exceptions.StatusTestException
import com.iideprived.rain.model.request.ErrorRequest
import com.iideprived.rain.model.response.GenericResponse

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