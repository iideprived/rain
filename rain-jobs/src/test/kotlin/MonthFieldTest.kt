package model.jobs

import com.iideprived.rain.model.jobs.com.iideprived.rain.jobs.CronField
import com.iideprived.rain.model.jobs.com.iideprived.rain.jobs.MonthField
import java.util.stream.Stream

class MonthFieldTest : CronFieldTest() {

    override val min: Int = 1
    override val max: Int = 12

    override fun createCronField(fieldExpr: String): CronField {
        return MonthField(fieldExpr)
    }

    override fun validTestCases(): Stream<ValidTestCase> {
        return Stream.of(
            ValidTestCase("*", (min..max).toSet()),
            ValidTestCase("*/4", setOf(1, 5, 9)),
            ValidTestCase("6-9", (6..9).toSet()),
            ValidTestCase("12", setOf(12)),
            ValidTestCase("3,6,9,12", setOf(3, 6, 9, 12))
        )
    }

    override fun invalidTestCases(): Stream<InvalidTestCase> {
        return Stream.of(
            InvalidTestCase("*/-1", "Step value must be positive"),
            InvalidTestCase("0", "out of bounds"),
            InvalidTestCase("13", "out of bounds"),
            InvalidTestCase("10-5", "greater than end value"),
            InvalidTestCase("month", "Invalid value")
        )
    }
}