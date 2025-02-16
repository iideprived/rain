package model.jobs

import com.iideprived.rain.model.jobs.com.iideprived.rain.jobs.CronField
import com.iideprived.rain.model.jobs.com.iideprived.rain.jobs.DayOfMonthField
import java.util.stream.Stream

class DayOfMonthFieldTest : CronFieldTest() {

    override val min: Int = 1
    override val max: Int = 31

    override fun createCronField(fieldExpr: String): CronField {
        return DayOfMonthField(fieldExpr)
    }

    override fun validTestCases(): Stream<ValidTestCase> {
        return Stream.of(
            ValidTestCase("*", (min..max).toSet()),
            ValidTestCase("*/10", setOf(1, 11, 21, 31)),
            ValidTestCase("1-15", (1..15).toSet()),
            ValidTestCase("31", setOf(31)),
            ValidTestCase("5,10,15", setOf(5, 10, 15))
        )
    }

    override fun invalidTestCases(): Stream<InvalidTestCase> {
        return Stream.of(
            InvalidTestCase("*/0", "Step value must be positive"),
            InvalidTestCase("0", "out of bounds"),
            InvalidTestCase("32", "out of bounds"),
            InvalidTestCase("20-10", "greater than end value"),
            InvalidTestCase("abc", "Invalid value")
        )
    }
}