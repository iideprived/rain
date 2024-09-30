package com.iideprived.rain.model.jobs.implementation.jobs

import com.iideprived.rain.model.jobs.annotations.jobs.Job
import com.iideprived.rain.model.jobs.annotations.jobs.Schedule
import com.iideprived.rain.implementation.jobs.RainSchedulerConfig
import com.iideprived.rain.implementation.jobs.Timezone
import com.iideprived.rain.model.jobs.Cron
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import util.getScanResult
import util.isRainInitiated

val RainScheduler = createApplicationPlugin("RainScheduler", createConfiguration = ::RainSchedulerConfig) {
    val pluginConfig = pluginConfig
    // Find all classes annotated with @Job
    // For each class, find all methods annotated with @Scheduled
    // For each method, schedule the method to run at the specified time

    val methodToCron = getScanResult(pluginConfig.classLoader).getClassesWithAnnotation(Job::class.qualifiedName)
        .filter { classInfo -> isRainInitiated || !classInfo.packageName.startsWith("com.iideprived.rain")  }
        .filter { classInfo -> pluginConfig.scanPackages.isEmpty() || pluginConfig.scanPackages.any { classInfo.packageName.startsWith(it) } }
        // We have the classes annotated with @Job, now we need to find the methods annotated with @Scheduled
        .flatMap { classInfo ->
            classInfo.declaredMethodInfo
                .filter { method ->
                    method.annotationInfo.any { it.classInfo.simpleName == Schedule::class.simpleName }
                }
        }
        // Ensure all methods have no parameters (we can't pass parameters to a scheduled job)
        // Throw an exception if a method has parameters
        .onEach { method ->
            if (method.parameterInfo.isNotEmpty()) {
                throw IllegalArgumentException("Method ${method.name} of class ${method.classInfo.simpleName} has parameters. Scheduled methods cannot have parameters.")
            }
        }
        .associateWith { method ->
            val cronString: String
            val timezone: Timezone
            // Map the method to the Cron expression
            val scheduleAnnotationValues = method.annotationInfo.first { it.classInfo.simpleName == Schedule::class.simpleName }.parameterValues
            cronString = (scheduleAnnotationValues.firstOrNull()?.value
                ?: throw IllegalArgumentException("Cron expression not found for method ${method.name} of class ${method.classInfo.simpleName}")).toString()

            timezone =
                Timezone.valueOf((scheduleAnnotationValues.lastOrNull()?.value
                    ?: throw IllegalArgumentException("Timezone not found for method ${method.name} of class ${method.classInfo.simpleName}")).toString().substringAfterLast('.'))

            Cron(cronString, timezone)
        }

    if (methodToCron.isEmpty()) return@createApplicationPlugin

    application.launch(Dispatchers.IO) {
        supervisorScope {
            for ((method, cron) in methodToCron) {
                launch {
                    cron.scheduleJob {
                        val classInstance = pluginConfig.createInstance(pluginConfig.classLoader.loadClass(method.classInfo.name))
                        val declaringClass = classInstance.javaClass
                        declaringClass.getDeclaredMethod(method.name).invoke(classInstance)
                    }
                }
            }
        }
    }
}
