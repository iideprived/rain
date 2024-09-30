package model.jobs

import com.iideprived.rain.implementation.jobs.Timezone
import com.iideprived.rain.model.jobs.Cron
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.util.stream.Stream
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CronTest {

    // Helper data classes for parameterized tests
    data class ValidTestCase(val cronExpression: String, val from: LocalDateTime, val expected: LocalDateTime)
    data class InvalidTestCase(val cronExpression: String, val expectedExceptionMessage: String)

    @ParameterizedTest
    @MethodSource("validTestCases")
    fun `test next execution time with valid expressions`(testCase: ValidTestCase) {
        val cron = Cron(testCase.cronExpression, Timezone.UTC)
        val nextTime = cron.nextExecutionTime(testCase.from)
        assertEquals(testCase.expected, nextTime, "Cron expression '${testCase.cronExpression}' did not return expected next execution time.")
    }

    @ParameterizedTest
    @MethodSource("invalidTestCases")
    fun `test invalid cron expressions`(testCase: InvalidTestCase) {
        val exception = assertThrows<IllegalArgumentException> {
            Cron(testCase.cronExpression, Timezone.UTC)
        }
        assertTrue(exception.message!!.contains(testCase.expectedExceptionMessage),
            "Expected exception message to contain '${testCase.expectedExceptionMessage}' but was '${exception.message}'.")
    }

    @Test
    fun `test scheduleJob runs the block at the correct next execution time`() = runBlocking {
        val cron = Cron("0 0 1 1 *", Timezone.UTC) // January 1st at 00:00
        val from = LocalDateTime.of(2023, 12, 31, 23, 59)
        val expectedTime = LocalDateTime.of(2024, 1, 1, 0, 0)
        val nextTime = cron.nextExecutionTime(from)
        assertEquals(expectedTime, nextTime)
    }

    companion object {
        @JvmStatic
        fun validTestCases(): Stream<ValidTestCase> {
            return Stream.of(
                // Every Minute
                ValidTestCase(
                    cronExpression = "* * * * *",
                    from = LocalDateTime.of(2023, 9, 27, 12, 0),
                    expected = LocalDateTime.of(2023, 9, 27, 12, 1)
                ),
                // Specific Minute
                ValidTestCase(
                    cronExpression = "30 * * * *",
                    from = LocalDateTime.of(2023, 9, 27, 12, 25),
                    expected = LocalDateTime.of(2023, 9, 27, 12, 30)
                ),
                // Minute Passed
                ValidTestCase(
                    cronExpression = "15 * * * *",
                    from = LocalDateTime.of(2023, 9, 27, 12, 20),
                    expected = LocalDateTime.of(2023, 9, 27, 13, 15)
                ),
                // Specific Hour and Minute
                ValidTestCase(
                    cronExpression = "0 14 * * *",
                    from = LocalDateTime.of(2023, 9, 27, 15, 0),
                    expected = LocalDateTime.of(2023, 9, 28, 14, 0)
                ),
                // Specific Day of Week (Monday)
                ValidTestCase(
                    cronExpression = "0 9 * * 1",
                    from = LocalDateTime.of(2023, 9, 27, 8, 0), // Wednesday
                    expected = LocalDateTime.of(2023, 10, 2, 9, 0) // Next Monday
                ),
                // Specific Day of Month and Day of Week
                ValidTestCase(
                    cronExpression = "0 0 15 * 3", // 15th and Wednesday
                    from = LocalDateTime.of(2023, 9, 14, 0, 0), // Thursday
                    expected = LocalDateTime.of(2023, 11, 15, 0, 0) // Next matching Wednesday, 15th
                ),
                // Step Values
                ValidTestCase(
                    cronExpression = "*/15 * * * *", // Every 15 minutes
                    from = LocalDateTime.of(2023, 9, 27, 14, 7),
                    expected = LocalDateTime.of(2023, 9, 27, 14, 15)
                ),
                // List of Values
                ValidTestCase(
                    cronExpression = "0 9,12,15 * * *", // At 09:00, 12:00, 15:00
                    from = LocalDateTime.of(2023, 9, 27, 10, 0),
                    expected = LocalDateTime.of(2023, 9, 27, 12, 0)
                ),
                // Leap Year - Feb 29
                ValidTestCase(
                    cronExpression = "0 0 29 2 *", // February 29th at 00:00
                    from = LocalDateTime.of(2023, 2, 28, 23, 59),
                    expected = LocalDateTime.of(2024, 2, 29, 0, 0)
                ),
                ValidTestCase(
                    cronExpression = "0 0 29 2 *", // February 29th at 00:00
                    from = LocalDateTime.of(2024, 2, 28, 23, 59),
                    expected = LocalDateTime.of(2024, 2, 29, 0, 0)
                ),
                // Leap Year - Next Leap Day after current one
                ValidTestCase(
                    cronExpression = "0 0 29 2 *", // February 29th at 00:00
                    from = LocalDateTime.of(2024, 3, 1, 0, 0),
                    expected = LocalDateTime.of(2028, 2, 29, 0, 0)
                ),
                ValidTestCase(
                    cronExpression = "0 0 1 3 *", // March 1st at 00:00
                    from = LocalDateTime.of(2024, 3, 2, 0, 0),
                    expected = LocalDateTime.of(2025, 3, 1, 0, 0)
                )
            )
        }

        @JvmStatic
        fun invalidTestCases(): Stream<InvalidTestCase> {
            return Stream.of(
                // Impossible Date Combination
                InvalidTestCase(
                    cronExpression = "0 0 31 2 *",
                    expectedExceptionMessage = "No valid dates are possible"
                ),
                // Invalid Day and Month Combination
                InvalidTestCase(
                    cronExpression = "0 0 31 4 *",
                    expectedExceptionMessage = "No valid dates are possible"
                ),
                // Empty Cron Expression
                InvalidTestCase(
                    cronExpression = "",
                    expectedExceptionMessage = "Cron expression must consist of 5 fields"
                ),
                // Invalid Field Value
                InvalidTestCase(
                    cronExpression = "60 * * * *",
                    expectedExceptionMessage = "Invalid value in cron field"
                )
            )
        }
    }
}