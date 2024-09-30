package model.jobs

import com.iideprived.rain.model.jobs.CronField
import com.iideprived.rain.model.jobs.MinuteField
import java.util.stream.Stream

class MinuteFieldTest : CronFieldTest() {

    override val min: Int = 0
    override val max: Int = 59

    override fun createCronField(fieldExpr: String): CronField {
        return MinuteField(fieldExpr)
    }

    override fun validTestCases(): Stream<ValidTestCase> {
        return Stream.of(
            ValidTestCase("*", (min..max).toSet()),
            ValidTestCase("*/15", setOf(0, 15, 30, 45)),
            ValidTestCase("0-5", (0..5).toSet()),
            ValidTestCase("30", setOf(30)),
            ValidTestCase("10,20,30", setOf(10, 20, 30))
        )
    }

    override fun invalidTestCases(): Stream<InvalidTestCase> {
        return Stream.of(
            InvalidTestCase("*/0", "Step value must be positive"),
            InvalidTestCase("60", "out of bounds"),
            InvalidTestCase("-1", "out of bounds"),
            InvalidTestCase("30-20", "greater than end value"),
            InvalidTestCase("abc", "Invalid value")
        )
    }
}