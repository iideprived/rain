package com.iideprived.rain.model.jobs.com.iideprived.rain.jobs

import java.time.LocalDateTime

sealed class CronField(private val fieldExpr: String) {

    abstract val min: Int
    abstract val max: Int
    abstract fun getValue(dateTime: LocalDateTime): Int

    internal val allowedValues: Set<Int> by lazy { parseField(fieldExpr) }


    fun matches(value: Int): Boolean {
        return allowedValues.contains(value)
    }

    private fun parseField(fieldExpr: String) : Set<Int> {
        /*
            case 1: * -> add all values from min to max
            case 2: *\/n -> add all values from min to max with step n
            case 3: n -> add value n if it is in range
            case 4: n-m -> add all values from n to m
            else throw exception
         */
        val values = mutableSetOf<Int>()
        when {
            fieldExpr == "*" -> {
                // Any value between min and max
                values.addAll(min..max)
            }
            fieldExpr.startsWith("*/") -> {
                // Step values
                val stepValue = fieldExpr.substringAfter("*/").toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid step value in cron field '$fieldExpr'")
                if (stepValue <= 0) {
                    throw IllegalArgumentException("Step value must be positive in cron field '$fieldExpr'")
                }
                values.addAll(min..max step stepValue)
            }
            "," in fieldExpr -> {
                // List of values
                val parts = fieldExpr.split(",")
                for (part in parts) {
                    val value = part.toIntOrNull()
                        ?: throw IllegalArgumentException("Invalid value '$part' in cron field '$fieldExpr'")
                    validateValue(value)
                    values.add(value)
                }
            }
            fieldExpr.matches("^\\d+$".toRegex())-> {
                // Single value
                val value = fieldExpr.toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid value in cron field '$fieldExpr'")
                validateValue(value)
                values.add(value)
            }
            "-" in fieldExpr -> {
                // Range of values
                val (startStr, endStr) = fieldExpr.split("-")
                val start = startStr.toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid start value in cron field '$fieldExpr'")
                val end = endStr.toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid end value in cron field '$fieldExpr'")
                validateValue(start)
                validateValue(end)
                if (start > end) {
                    throw IllegalArgumentException("Start value $start is greater than end value $end in cron field '$fieldExpr'")
                }
                values.addAll(start..end)
            }
            else -> {
                throw IllegalArgumentException("Invalid cron field expression '$fieldExpr'")
            }
        }
        return values
    }

    private fun validateValue(value: Int) {
        if (value < min || value > max) {
            throw IllegalArgumentException("Invalid value in cron field '$fieldExpr': $value is out of bounds ($min-$max)")
        }
    }
}

class MinuteField(fieldExpr: String) : CronField(fieldExpr) {
    override val min: Int = 0
    override val max: Int = 59

    override fun getValue(dateTime: LocalDateTime): Int {
        return dateTime.minute
    }
}

class HourField(fieldExpr: String) : CronField(fieldExpr) {
    override val min: Int = 0
    override val max: Int = 23

    override fun getValue(dateTime: LocalDateTime): Int {
        return dateTime.hour
    }
}

class DayOfMonthField(fieldExpr: String) : CronField(fieldExpr) {
    override val min: Int = 1
    override val max: Int = 31

    override fun getValue(dateTime: LocalDateTime): Int {
        return dateTime.dayOfMonth
    }
}

class MonthField(fieldExpr: String) : CronField(fieldExpr) {
    override val min: Int = 1
    override val max: Int = 12

    override fun getValue(dateTime: LocalDateTime): Int {
        return dateTime.monthValue
    }
}

class DayOfWeekField(fieldExpr: String) : CronField(fieldExpr) {
    override val min: Int = 0 // 0 = Sunday
    override val max: Int = 6 // 6 = Saturday

    override fun getValue(dateTime: LocalDateTime): Int {
        return (dateTime.dayOfWeek.value % 7) // Adjust Sunday from 7 to 0
    }
}