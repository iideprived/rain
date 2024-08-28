package com.iideprived.di

import com.iideprived.rain.service.UserService
import com.iideprived.rain.service.impl.UserServiceImpl
import org.koin.dsl.module

internal val serviceModule = module {
    // Service dependencies can be defined here
    single<UserService> { UserServiceImpl() }
}
