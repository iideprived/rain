package routes

import com.iideprived.rain.core.model.request.ErrorRequest
import com.iideprived.rain.core.routes.ErrorRouter
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class ErrorRouterTest {
    private val errorRouter = ErrorRouter()

    @Test
    fun testGetError() {
        assertFailsWith<com.iideprived.rain.core.exceptions.GenericException> { errorRouter.getError() }
    }

    @Test
    fun testGetErrorCode() {
        assertFailsWith<com.iideprived.rain.core.exceptions.ErrorTestException> { errorRouter.getErrorCode() }
    }

    @Test
    fun testGetErrorStatus() {
        assertFailsWith<com.iideprived.rain.core.exceptions.StatusTestException> { errorRouter.getErrorStatus() }
    }

    @Test
    fun testGetErrorCodeAndStatus() {
        assertFailsWith<com.iideprived.rain.core.exceptions.ErrorStatusTestException> { errorRouter.getErrorCodeAndStatus() }
    }

    @Test
    fun testPostErrorGeneric() {
        assertFailsWith<com.iideprived.rain.core.exceptions.GenericException> { errorRouter.postError(ErrorRequest(hasStatus = false, hasCode = false)) }
    }

    @Test
    fun testPostErrorStatus() {
        assertFailsWith<com.iideprived.rain.core.exceptions.StatusTestException> { errorRouter.postError(ErrorRequest(hasStatus = true, hasCode = false)) }
    }

    @Test
    fun testPostErrorCode() {
        assertFailsWith<com.iideprived.rain.core.exceptions.ErrorTestException> { errorRouter.postError(ErrorRequest(hasStatus = false, hasCode = true)) }
    }

    @Test
    fun testPostErrorCodeAndStatus() {
        assertFailsWith<com.iideprived.rain.core.exceptions.ErrorStatusTestException> { errorRouter.postError(ErrorRequest(hasStatus = true, hasCode = true)) }
    }
}