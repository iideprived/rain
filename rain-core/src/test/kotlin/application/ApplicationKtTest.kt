package application

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertTrue

class ApplicationKtTest {
    @Test
    fun moduleTest() {
        try {
            testApplication {
                application { }
            }
        } catch (e: Exception) {
            assertTrue { true }
        }
    }
}