package model.jobs

import com.iideprived.rain.model.jobs.com.iideprived.rain.jobs.CronField
import com.iideprived.rain.model.jobs.com.iideprived.rain.jobs.DayOfWeekField
import java.util.stream.Stream

class DayOfWeekFieldTest : CronFieldTest() {

    override val min: Int = 0
    override val max: Int = 6

    override fun createCronField(fieldExpr: String): CronField {
        return DayOfWeekField(fieldExpr)
    }

    override fun validTestCases(): Stream<ValidTestCase> {
        return Stream.of(
            ValidTestCase("*", (min..max).toSet()),
            ValidTestCase("*/2", setOf(0, 2, 4, 6)),
            ValidTestCase("1-5", (1..5).toSet()),
            ValidTestCase("0", setOf(0)),
            ValidTestCase("1,3,5", setOf(1, 3, 5))
        )
    }

    override fun invalidTestCases(): Stream<InvalidTestCase> {
        return Stream.of(
            InvalidTestCase("*/0", "Step value must be positive"),
            InvalidTestCase("-1", "out of bounds"),
            InvalidTestCase("7", "out of bounds"),
            InvalidTestCase("5-2", "greater than end value"),
            InvalidTestCase("Sun", "Invalid value")
        )
    }
}