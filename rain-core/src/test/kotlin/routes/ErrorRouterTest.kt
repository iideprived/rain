package routes

import com.iideprived.rain.exceptions.ErrorStatusTestException
import com.iideprived.rain.exceptions.ErrorTestException
import com.iideprived.rain.exceptions.GenericException
import com.iideprived.rain.exceptions.StatusTestException
import com.iideprived.rain.model.request.ErrorRequest
import com.iideprived.rain.routes.ErrorRouter
import org.junit.Test
import kotlin.test.assertFailsWith

internal class ErrorRouterTest {
    private val errorRouter = ErrorRouter()

    @Test
    fun testGetError() {
        assertFailsWith<GenericException> { errorRouter.getError() }
    }

    @Test
    fun testGetErrorCode() {
        assertFailsWith<ErrorTestException> { errorRouter.getErrorCode() }
    }

    @Test
    fun testGetErrorStatus() {
        assertFailsWith<StatusTestException> { errorRouter.getErrorStatus() }
    }

    @Test
    fun testGetErrorCodeAndStatus() {
        assertFailsWith<ErrorStatusTestException> { errorRouter.getErrorCodeAndStatus() }
    }

    @Test
    fun testPostErrorGeneric() {
        assertFailsWith<GenericException> { errorRouter.postError(ErrorRequest(hasStatus = false, hasCode = false)) }
    }

    @Test
    fun testPostErrorStatus() {
        assertFailsWith<StatusTestException> { errorRouter.postError(ErrorRequest(hasStatus = true, hasCode = false)) }
    }

    @Test
    fun testPostErrorCode() {
        assertFailsWith<ErrorTestException> { errorRouter.postError(ErrorRequest(hasStatus = false, hasCode = true)) }
    }

    @Test
    fun testPostErrorCodeAndStatus() {
        assertFailsWith<ErrorStatusTestException> { errorRouter.postError(ErrorRequest(hasStatus = true, hasCode = true)) }
    }
}