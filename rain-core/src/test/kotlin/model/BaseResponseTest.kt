package model

import com.iideprived.rain.model.response.BaseResponse
import com.iideprived.rain.model.response.GenericResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class BaseResponseTest {

    @Test
    fun testAsSuccess() {
        val response = object : BaseResponse() {}
        response.asSuccess()

        assertEquals(200, response.statusCode)
        assertEquals(BaseResponse.DEFAULT_SUCCESS_STATUS_MESSAGE, response.statusMessage)
        assertEquals(null, response.errorCode)
        assertEquals(null, response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testAsFailure() {
        val response = object : BaseResponse() {}
        val exception = Exception("Test error")
        response.asFailure(exception)

        assertEquals(BaseResponse.DEFAULT_FAILURE_STATUS_CODE, response.statusCode)
        assertEquals(BaseResponse.DEFAULT_FAILURE_STATUS_MESSAGE, response.statusMessage)
        assertEquals(BaseResponse.DEFAULT_ERROR_CODE, response.errorCode)
        assertEquals("Test error", response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testAsFailureWithCustomErrorCodeAndStatusCode() {
        val response = object : BaseResponse() {}
        val exception = Exception("Test error")
        val customErrorCode = "CUSTOM_ERROR"
        val customStatusCode = 500
        response.asFailure(exception, customErrorCode, customStatusCode)

        assertEquals(customStatusCode, response.statusCode)
        assertEquals(BaseResponse.DEFAULT_FAILURE_STATUS_MESSAGE, response.statusMessage)
        assertEquals(customErrorCode, response.errorCode)
        assertEquals("Test error", response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testCreateInstance() {
        val response: BaseResponse = BaseResponse.createInstance<GenericResponse>()
        assertNotNull(response)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testSuccessStaticMethod() {
        val response: BaseResponse = BaseResponse.success<GenericResponse>()
        assertEquals(200, response.statusCode)
        assertEquals(BaseResponse.DEFAULT_SUCCESS_STATUS_MESSAGE, response.statusMessage)
        assertEquals(null, response.errorCode)
        assertEquals(null, response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }

    @Test
    fun testFailureStaticMethod() {
        val exception = Exception("Test error")
        val response: BaseResponse = BaseResponse.failure<GenericResponse>(exception)
        assertEquals(BaseResponse.DEFAULT_FAILURE_STATUS_CODE, response.statusCode)
        assertEquals(BaseResponse.DEFAULT_FAILURE_STATUS_MESSAGE, response.statusMessage)
        assertEquals(BaseResponse.DEFAULT_ERROR_CODE, response.errorCode)
        assertEquals("Test error", response.errorMessage)
        assertNotNull(response.timestamp) // Accessing timestamp to avoid "unused" warning
    }
}