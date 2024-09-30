package model.jobs

import com.iideprived.rain.model.jobs.CronField
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class CronFieldTest {

    abstract val min: Int
    abstract val max: Int
    abstract fun createCronField(fieldExpr: String): CronField

    data class ValidTestCase(val fieldExpr: String, val expectedValues: Set<Int>)
    data class InvalidTestCase(val fieldExpr: String, val expectedException: String)
    
    @ParameterizedTest
    @MethodSource("validTestCases")
    fun `test valid field expressions`(testCase: ValidTestCase) {
        val cronField = createCronField(testCase.fieldExpr)
        assertEquals(testCase.expectedValues, cronField.allowedValues)
    }

    @ParameterizedTest
    @MethodSource("invalidTestCases")
    fun `test invalid field expressions`(testCase: InvalidTestCase) {
        assertThrows<IllegalArgumentException> {
            createCronField(testCase.fieldExpr).allowedValues
        }
    }

    @Test
    fun `test matches method`() {
        val cronField = createCronField("*/10")
        for (i in min..max) {
            if (i % 10 == 0) {
                assertTrue(cronField.matches(i + min), "Expected ${i + min} to match")
            } else {
                assertFalse(cronField.matches(i + min), "Expected ${i + min} not to match")
            }
        }
    }

    @Test
    fun `test getValue method`() {
        val dateTime = LocalDateTime.of(2021, 12, 31, 23, 59)
        val cronField = createCronField("*")
        val value = cronField.getValue(dateTime)
        assertTrue(value in min..max, "Value $value should be between $min and $max")
    }

    // Abstract methods to provide test cases
    abstract fun validTestCases(): Stream<ValidTestCase>
    abstract fun invalidTestCases(): Stream<InvalidTestCase>
}