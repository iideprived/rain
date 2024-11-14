package com.iideprived.rain.jobs

import com.iideprived.rain.model.jobs.annotations.jobs.Job
import com.iideprived.rain.model.jobs.annotations.jobs.Schedule
import com.iideprived.rain.implementation.jobs.Timezone

@Job
internal class TestJob {

    private val messages = listOf(
        "Hello, world!",
        "This is a test message!",
        "Apples are red, violets are blue, I'm a poet, and I didn't even know it!",
        "My name is Rain, and I'm here to stay!",
    )

    private var selectedMessage = messages.random()

    @Schedule(cron = "0 0 * * *", timezone = Timezone.EST)
    fun test() {
        println(selectedMessage)
        selectedMessage = messages.random()
    }
}