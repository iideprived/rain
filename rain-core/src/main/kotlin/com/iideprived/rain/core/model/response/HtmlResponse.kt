package com.iideprived.rain.model.response

data class HtmlResponse(
    override val body: String,
    override val headers: Map<String, String> = mapOf("Content-Type" to "text/html"),
) : ControlledResponse<String>(body, headers, contentType = "text/html")
