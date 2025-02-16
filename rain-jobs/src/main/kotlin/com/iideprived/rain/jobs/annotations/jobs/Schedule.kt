package com.iideprived.rain.model.jobs.annotations.jobs

import com.iideprived.rain.implementation.jobs.Timezone

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Schedule(
    val cron: String,
    val timezone: Timezone = Timezone.UTC,
)

