package com.iideprived.rain.core.model.response

data class TextResponse(
    override val body: String,
    override val headers: Map<String, String> = mapOf("Content-Type" to "text/plain"),
) : ControlledResponse<String>(body, headers, contentType = "text/plain")
