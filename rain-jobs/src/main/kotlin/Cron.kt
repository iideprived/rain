package com.iideprived.rain.model.jobs

// Cron.kt
import com.iideprived.rain.implementation.jobs.Timezone
import kotlinx.coroutines.delay
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Cron(private val expression: String, private val timezone: Timezone = Timezone.UTC) {

    private val fields: List<CronField> = parseExpression(expression)
    private val validDatePairs: Set<Pair<Int, Int>> = computeValidDatePairs()

    init {
        if (validDatePairs.isEmpty()) {
            throw IllegalArgumentException("No valid dates are possible with the given cron expression '$expression'")
        }
        fields.forEach { it.allowedValues }
    }

    /**
     * Calculates the next execution time after the given time.
     * @param from The time from which to calculate the next execution time.
     * @return The next execution time as a LocalDateTime.
     * @throws IllegalArgumentException if no valid execution time is found within the search limit.
     */
    fun nextExecutionTime(from: LocalDateTime = LocalDateTime.now(timezone.toZonedOffset())): LocalDateTime {
        val searchLimit = from.plusYears(28) // Cover all leap year scenarios
        var nextTime = from.truncatedTo(ChronoUnit.MINUTES).plusMinutes(1) // Start from the next minute

        while (nextTime <= searchLimit) {
            try {
                nextTime = adjustToNextValidMinute(nextTime)
                nextTime = adjustToNextValidHour(nextTime)
                nextTime = adjustToNextValidDate(nextTime, searchLimit)

                // All fields match
                return nextTime
            } catch (e: DateTimeException) {
                // Handle invalid dates (e.g., February 30)
                nextTime = nextTime.plusDays(1)
                continue
            }
        }

        throw IllegalArgumentException("No valid execution time found within the next 28 years for cron expression '$expression'")
    }

    /**
     * Schedules a job to run based on the cron expression.
     * @param block The block of code to execute.
     */
    suspend fun scheduleJob(block: () -> Unit) {
        while (true) {
            val timeNow = LocalDateTime.now(timezone.toZonedOffset())
            val nextExecutionTime = nextExecutionTime(timeNow)
            val delayMillis = ChronoUnit.MILLIS.between(timeNow, nextExecutionTime)
            if (delayMillis <= 0) {
                // The next execution time is in the past, so we missed it
                continue
            }
            delay(delayMillis)
            block()
        }
    }

    private fun adjustToNextValidMinute(dateTime: LocalDateTime): LocalDateTime {
        val minuteField = fields[0]
        val nextMinute = getNextValidValue(minuteField, dateTime.minute)
        return if (nextMinute == dateTime.minute) {
            dateTime
        } else if (nextMinute > dateTime.minute) {
            dateTime.withMinute(nextMinute).withSecond(0).withNano(0)
        } else {
            // Next valid minute is in the next hour
            dateTime.plusHours(1).withMinute(nextMinute).withSecond(0).withNano(0)
        }
    }

    private fun adjustToNextValidHour(dateTime: LocalDateTime): LocalDateTime {
        val hourField = fields[1]
        val nextHour = getNextValidValue(hourField, dateTime.hour)
        return if (nextHour == dateTime.hour) {
            dateTime
        } else if (nextHour > dateTime.hour) {
            dateTime.withHour(nextHour)
        } else {
            // Next valid hour is on the next day
            dateTime.plusDays(1).withHour(nextHour)
        }
    }



    private fun adjustToNextValidDate(currentDateTime: LocalDateTime, searchLimit: LocalDateTime): LocalDateTime {

        if (matchesDayOfWeek(currentDateTime) && matchesDayAndMonth(currentDateTime)){
            return currentDateTime
        }

        val sortedDatePairs = validDatePairs.sortedWith(compareBy({ it.second }, { it.first }))
        val yearRange = currentDateTime.year .. searchLimit.year

        for (year in yearRange) {
            for (datePair in sortedDatePairs) {
                try {
                    val candidateDate = LocalDateTime.of(year, datePair.second, datePair.first, 0, 0)
                    if (candidateDate.isAfter(currentDateTime) && matchesDayOfWeek(candidateDate)) {
                        return candidateDate.withHour(currentDateTime.hour).withMinute(currentDateTime.minute)
                    }
                } catch (e: DateTimeException) {
                    // Skip invalid dates (e.g., February 29 on non-leap years)
                    continue
                }
            }
        }

        throw IllegalArgumentException("No valid execution time found within the next 28 years for cron expression '$expression'")
    }

    private fun matchesDayOfWeek(date: LocalDateTime) = fields[4].matches(date.dayOfWeek.value % 7)
    private fun matchesDayAndMonth(date: LocalDateTime) = fields[2].matches(date.dayOfMonth) && fields[3].matches(date.monthValue)

    private fun getNextValidValue(field: CronField, currentValue: Int): Int {
        val sortedValues = field.allowedValues.sorted()
        val nextValues = sortedValues.filter { it >= currentValue }
        return if (nextValues.isNotEmpty()) {
            nextValues.first()
        } else {
            // Wrap around to the first allowed value
            sortedValues.first()
        }
    }

    private fun computeValidDatePairs(): Set<Pair<Int, Int>> {
        val impossibleDates = setOf(
            Pair(30, 2), Pair(31, 2),
            Pair(31, 4), Pair(31, 6), Pair(31, 9), Pair(31, 11)
        )

        val allowedDays = fields[2].allowedValues
        val allowedMonths = fields[3].allowedValues

        val datePairs = allowedDays.flatMap { day ->
            allowedMonths.map { month ->
                Pair(day, month)
            }
        }.toSet()

        return datePairs - impossibleDates
    }

    private fun parseExpression(expression: String): List<CronField> {
        val parts = expression.trim().split("\\s+".toRegex())
        if (parts.size != 5) {
            throw IllegalArgumentException("Cron expression must consist of 5 fields")
        }
        return listOf(
            MinuteField(parts[0]),
            HourField(parts[1]),
            DayOfMonthField(parts[2]),
            MonthField(parts[3]),
            DayOfWeekField(parts[4])
        )
    }
}