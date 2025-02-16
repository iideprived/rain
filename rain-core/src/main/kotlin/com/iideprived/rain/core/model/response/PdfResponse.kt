package com.iideprived.rain.core.model.response

data class PdfResponse(
    override val body: ByteArray,
    override val headers: Map<String, String> = mapOf("Content-Type" to "application/pdf"),
) : ControlledResponse<ByteArray>(body, headers, contentType = "application/pdf") {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PdfResponse

        if (!body.contentEquals(other.body)) return false
        if (headers != other.headers) return false
        if (contentType != other.contentType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = body.contentHashCode()
        result = 31 * result + headers.hashCode()
        result = 31 * result + contentType.hashCode()
        return result
    }
}