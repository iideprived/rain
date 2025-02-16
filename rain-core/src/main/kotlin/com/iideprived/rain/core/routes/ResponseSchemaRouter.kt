package com.iideprived.rain.core.routes

import com.iideprived.rain.core.annotations.routing.Service
import com.iideprived.rain.core.annotations.routing.httpmethod.Get
import com.iideprived.rain.core.model.User
import com.iideprived.rain.core.model.response.*
import com.iideprived.rain.core.model.response.UpdateUserResponse
import io.ktor.util.*


@Service("/response")
internal class ResponseSchemaRouter {


    @Get("/json")
    fun getJsonGenericResponse(): JsonResponse<UpdateUserResponse> =
        JsonResponse(
            UpdateUserResponse(
                User(
            id = 5,
            name = "John Doe",
            email = "john.doe@email.com"
        )
            )
        )

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
        return PdfResponse("PDF content goes here".encodeToByteArray())
    }

}