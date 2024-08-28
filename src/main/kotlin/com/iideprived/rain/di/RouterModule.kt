package com.iideprived.di

import com.iideprived.rain.routes.UserRouter
import org.koin.dsl.module

internal val routerModule = module {
    // Router dependencies can be defined here
    // Example:
    // single { createRouter(get()) }

    single<UserRouter> { UserRouter(get()) }
}