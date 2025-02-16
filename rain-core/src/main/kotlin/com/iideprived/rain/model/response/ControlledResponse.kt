package com.iideprived.rain.model.response

import io.ktor.http.*

open class ControlledResponse<T>(
    open val body: T,
    open val headers: Map<String, String> = emptyMap(),
    open var contentType: String = "application/json",
    open var status: HttpStatusCode = HttpStatusCode.OK
) {

    fun withHeaders(headers: Map<String, String>): ControlledResponse<T> {
        return this.apply {
            this.headers.toMutableMap().putAll(headers)
        }
    }

    fun withHeaders(vararg headers: Pair<String, String>): ControlledResponse<T> {
        return withHeaders(headers.toMap())
    }

    fun status(status: HttpStatusCode): ControlledResponse<T> {
        return this.apply {
            this.status = status
        }
    }

    fun contentType(contentType: String): ControlledResponse<T> {
        return this.apply {
            this.contentType = contentType
        }
    }

}