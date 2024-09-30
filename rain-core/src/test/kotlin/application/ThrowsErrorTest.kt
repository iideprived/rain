package application

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ThrowsErrorTest {

    private val errorCodePattern = "\"errorCode\"\\s*:\\s*\"([^\"]+)\"".toRegex()
    private val errorMessagePattern = "\"errorMessage\"\\s*:\\s*\"([^\"]+)\"".toRegex()
    private val statusCodePattern = "\"statusCode\"\\s*:\\s*(\\d+)".toRegex()

    private suspend fun parseGenericResponse(response: HttpResponse): Triple<String, String, Int> {
        val responseBody = response.bodyAsText() // Get the response as a text
        val errorCode = errorCodePattern.find(responseBody)?.groups?.get(1)?.value ?: "UnknownErrorCode"
        val errorMessage = errorMessagePattern.find(responseBody)?.groups?.get(1)?.value ?: "UnknownErrorMessage"
        val statusCode = statusCodePattern.find(responseBody)?.groups?.get(1)?.value?.toInt() ?: 400

        return Triple(errorCode, errorMessage, statusCode)
    }

    @Test
    fun testErrorEndpoint() {
        testApplication {
            client.get("/error").apply {
                assertEquals(HttpStatusCode.BadRequest, this.status)
            }
        }
    }

    @Test
    fun testErrorCodeEndpoint() {
        testApplication {
            client.get("/error/code").apply {
                assertEquals(HttpStatusCode.BadRequest, this.status)
                val (errorCode, _, _) = parseGenericResponse(this)
                assertEquals("TEST_ERROR", errorCode)
            }
        }
    }

    @Test
    fun testErrorStatusEndpoint() {
        testApplication {
            client.get("/error/status").apply {
                assertEquals(HttpStatusCode.Forbidden, this.status)
                val (_, _, statusCode) = parseGenericResponse(this)
                assertEquals(HttpStatusCode.Forbidden.value, statusCode)
            }
        }
    }

    @Test
    fun testErrorCodeAndStatusEndpoint() {
        testApplication {
            client.get("/error/codeAndStatus").apply {
                assertEquals(HttpStatusCode.InternalServerError, this.status)
                val (errorCode, _, statusCode) = parseGenericResponse(this)
                assertEquals("TEST_ERROR_STATUS", errorCode)
                assertEquals(HttpStatusCode.InternalServerError.value, statusCode)
            }
        }
    }

    @Test
    fun testPostErrorGeneric() {
        testApplication {
            client.post("/error") {
                contentType(ContentType.Application.Json)
                setBody("""{"hasCode": false, "hasStatus": false}""")
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, this.status)
            }
        }
    }
    @Test
    fun testPostErrorGenericNull() {
        testApplication {
            client.post("/error") {
                contentType(ContentType.Application.Json)
                setBody("""{"hasCode": null, "hasStatus": null}""")
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, this.status)
            }
        }
    }

    @Test
    fun testPostErrorStatus() {
        testApplication {
            client.post("/error") {
                contentType(ContentType.Application.Json)
                setBody("""{"hasCode": false, "hasStatus": true}""")
            }.apply {
                assertEquals(HttpStatusCode.Forbidden, this.status)
                val (_, _, statusCode) = parseGenericResponse(this)
                assertEquals(HttpStatusCode.Forbidden.value, statusCode)
            }
        }
    }

    @Test
    fun testPostErrorCode() {
        testApplication {
            client.post("/error") {
                contentType(ContentType.Application.Json)
                setBody("""{"hasCode": true, "hasStatus": false}""")
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, this.status)
                val (errorCode, _, _) = parseGenericResponse(this)
                assertEquals("TEST_ERROR", errorCode)
            }
        }
    }

    @Test
    fun testPostErrorCodeAndStatus() {
        testApplication {
            client.post("/error") {
                contentType(ContentType.Application.Json)
                setBody("""{"hasCode": true, "hasStatus": true}""")
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, this.status)
                val (errorCode, _, statusCode) = parseGenericResponse(this)
                assertEquals("TEST_ERROR_STATUS", errorCode)
                assertEquals(HttpStatusCode.InternalServerError.value, statusCode)
            }
        }
    }
}