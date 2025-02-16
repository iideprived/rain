package model.jobs

import com.iideprived.rain.model.jobs.com.iideprived.rain.jobs.CronField
import com.iideprived.rain.model.jobs.com.iideprived.rain.jobs.HourField
import java.util.stream.Stream

class HourFieldTest : CronFieldTest() {

    override val min: Int = 0
    override val max: Int = 23

    override fun createCronField(fieldExpr: String): CronField {
        return HourField(fieldExpr)
    }

    override fun validTestCases(): Stream<ValidTestCase> {
        return Stream.of(
            ValidTestCase("*", (min..max).toSet()),
            ValidTestCase("*/6", setOf(0, 6, 12, 18)),
            ValidTestCase("0-12", (0..12).toSet()),
            ValidTestCase("23", setOf(23)),
            ValidTestCase("5,10,15", setOf(5, 10, 15))
        )
    }

    override fun invalidTestCases(): Stream<InvalidTestCase> {
        return Stream.of(
            InvalidTestCase("*/-1", "Step value must be positive"),
            InvalidTestCase("24", "out of bounds"),
            InvalidTestCase("-1", "out of bounds"),
            InvalidTestCase("15-10", "greater than end value"),
            InvalidTestCase("xyz", "Invalid value")
        )
    }
}