package application

import com.iideprived.rain.core.module
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseSchemaTest {

    @Test
    fun testGetJsonGenericResponse() {
        testApplication {
            application { module() }
            client.get("/response/json").apply {
                assertEquals(HttpStatusCode.OK, this.status)
                val responseBody = this.bodyAsText()
                assertTrue(responseBody.contains("John Doe"))
            }
        }
    }

    @Test
    fun testGetTextResponse() {
        testApplication {
            application { module() }
            client.get("/response/text").apply {
                assertEquals(HttpStatusCode.OK, this.status)
                val responseBody = this.bodyAsText()
                assertEquals("Hello, World!", responseBody)
            }
        }
    }

    @Test
    fun testGetHtmlResponse() {
        testApplication {
            application { module() }
            client.get("/response/html").apply {
                assertEquals(HttpStatusCode.OK, this.status)
                val responseBody = this.bodyAsText()
                assertEquals("<html><body>Hello, World!</body></html>", responseBody)
            }
        }
    }

    @Test
    fun testGetPdfResponse() {
        testApplication {
            application { module() }
            client.get("/response/pdf").apply {
                assertEquals(HttpStatusCode.OK, this.status)
                val bytes = this.body<ByteArray>().decodeToString()
                assertEquals(bytes, "PDF content goes here".encodeToByteArray().decodeToString())
            }
        }
    }
}