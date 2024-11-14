package model

import com.iideprived.rain.core.model.response.GenericResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class BaseResponseTest {

    @Test
    fun testAsSuccess() {
        val response = object : com.iideprived.rain.core.model.response.BaseResponse() {}
        response.asSuccess()

        assertEquals(200, response.statusCode)
        assertEquals(com.iideprived.rain.core.model.response.BaseResponse.DEFAULT_SUCCESS_STATUS_MESSAGE, response.statusMessage)
        assertEquals(null, response.errorCode)
        assertEquals(null, response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testAsFailure() {
        val response = object : com.iideprived.rain.core.model.response.BaseResponse() {}
        val exception = Exception("Test error")
        response.asFailure(exception)

        assertEquals(com.iideprived.rain.core.model.response.BaseResponse.DEFAULT_FAILURE_STATUS_CODE, response.statusCode)
        assertEquals(com.iideprived.rain.core.model.response.BaseResponse.DEFAULT_FAILURE_STATUS_MESSAGE, response.statusMessage)
        assertEquals(com.iideprived.rain.core.model.response.BaseResponse.DEFAULT_ERROR_CODE, response.errorCode)
        assertEquals("Test error", response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testAsFailureWithCustomErrorCodeAndStatusCode() {
        val response = object : com.iideprived.rain.core.model.response.BaseResponse() {}
        val exception = Exception("Test error")
        val customErrorCode = "CUSTOM_ERROR"
        val customStatusCode = 500
        response.asFailure(exception, customErrorCode, customStatusCode)

        assertEquals(customStatusCode, response.statusCode)
        assertEquals(com.iideprived.rain.core.model.response.BaseResponse.DEFAULT_FAILURE_STATUS_MESSAGE, response.statusMessage)
        assertEquals(customErrorCode, response.errorCode)
        assertEquals("Test error", response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testCreateInstance() {
        val response: com.iideprived.rain.core.model.response.BaseResponse = com.iideprived.rain.core.model.response.BaseResponse.createInstance<GenericResponse>()
        assertNotNull(response)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testSuccessStaticMethod() {
        val response: com.iideprived.rain.core.model.response.BaseResponse = com.iideprived.rain.core.model.response.BaseResponse.success<GenericResponse>()
        assertEquals(200, response.statusCode)
        assertEquals(com.iideprived.rain.core.model.response.BaseResponse.DEFAULT_SUCCESS_STATUS_MESSAGE, response.statusMessage)
        assertEquals(null, response.errorCode)
        assertEquals(null, response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testFailureStaticMethod() {
        val exception = Exception("Test error")
        val response: com.iideprived.rain.core.model.response.BaseResponse = com.iideprived.rain.core.model.response.BaseResponse.failure<GenericResponse>(exception)
        assertEquals(com.iideprived.rain.core.model.response.BaseResponse.DEFAULT_FAILURE_STATUS_CODE, response.statusCode)
        assertEquals(com.iideprived.rain.core.model.response.BaseResponse.DEFAULT_FAILURE_STATUS_MESSAGE, response.statusMessage)
        assertEquals(com.iideprived.rain.core.model.response.BaseResponse.DEFAULT_ERROR_CODE, response.errorCode)
        assertEquals("Test error", response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }
}