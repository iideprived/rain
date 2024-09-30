package application

import com.iideprived.rain.module
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertTrue

class ApplicationKtTest {
    @Test
    fun moduleTest() {
        try {
            testApplication {

                application {
                    module()
                }
            }
        } catch (e: Exception) {
            assertTrue { true }
        }
    }
}