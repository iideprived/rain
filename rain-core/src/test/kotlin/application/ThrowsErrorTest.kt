package application

import com.iideprived.rain.autoroute.Rain
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ThrowsErrorTest {

    val errorCodePattern = "\"errorCode\"\\s*:\\s*\"([^\"]+)\"".toRegex()
    val errorMessagePattern = "\"errorMessage\"\\s*:\\s*\"([^\"]+)\"".toRegex()
    val statusCodePattern = "\"statusCode\"\\s*:\\s*(\\d+)".toRegex()

    suspend fun parseGenericResponse(response: HttpResponse): Triple<String, String, Int> {
        val responseBody = response.bodyAsText() // Get the response as a text
        val errorCode = errorCodePattern.find(responseBody)?.groups?.get(1)?.value ?: "UnknownErrorCode"
        val errorMessage = errorMessagePattern.find(responseBody)?.groups?.get(1)?.value ?: "UnknownErrorMessage"
        val statusCode = statusCodePattern.find(responseBody)?.groups?.get(1)?.value?.toInt() ?: 400

        return Triple(errorCode, errorMessage, statusCode)
    }

    @Test
    fun testErrorEndpoint() {
        testApplication {
            install(Rain)
            client.get("/error").apply {
                assertEquals(HttpStatusCode.BadRequest, this.status)
            }
        }
    }

    @Test
    fun testErrorCodeEndpoint() {
        testApplication {
            install(Rain)
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
            install(Rain)
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
            install(Rain)
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
            install(Rain)
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
            install(Rain)
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
            install(Rain)
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
            install(Rain)
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
            install(Rain)
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