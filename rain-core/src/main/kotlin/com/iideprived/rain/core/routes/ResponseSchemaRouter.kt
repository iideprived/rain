package com.iideprived.rain.routes

import com.iideprived.rain.annotations.routing.Service
import com.iideprived.rain.annotations.routing.httpmethod.Get
import com.iideprived.rain.model.User
import com.iideprived.rain.model.response.*
import com.iideprived.rain.model.response.UpdateUserResponse
import io.ktor.util.*

@Service("/response")
internal class ResponseSchemaRouter {


    @Get("/")
    fun getJsonGenericGenericResponse(): JsonResponse<UpdateUserResponse> =
        JsonResponse(UpdateUserResponse(User(
            id = 5,
            name = "John Doe",
            email = "john.doe@email.com"
        )))

    @Get("/text")
    fun getTextResponse(): TextResponse {
        return TextResponse("Hello, World!")
    }

    @Get("/html")
    fun getHtmlResponse(): HtmlResponse {
        return HtmlResponse("<html><body>Hello, World!</body></html>")
    }

    @Get("/pdf")
    fun getPdfResponse(): PdfResponse {
        return PdfResponse("PDF content goes here".decodeBase64Bytes())
    }

}