package com.iideprived.rain.routes

import com.iideprived.rain.annotations.*
import com.iideprived.rain.model.User
import com.iideprived.rain.model.response.CreateUserResponse
import com.iideprived.rain.model.response.GetAllUsersResponse
import com.iideprived.rain.model.response.GetUserByIdResponse
import com.iideprived.rain.model.response.UpdateUserResponse
import com.iideprived.rain.service.UserService
import com.iideprived.rain.service.impl.UserServiceImpl

@Service("/users")
internal class UserRouter(private val userService: UserService = UserServiceImpl()) {

    @Get
    internal fun getAllUsers() = GetAllUsersResponse(userService.getAllUsers())

    @Get("/{id}")
    internal fun getUserById(@Path("id") id: Int) = when (val user = userService.getUserById(id)) {
        null -> GetUserByIdResponse()
        else -> GetUserByIdResponse(user)
    }

    @Post
    internal fun createUser(@Body user: User) = CreateUserResponse(userService.createUser(user))

    @Put("/{id}")
    internal fun updateUserById(@Path("id") id: Int, @Body user: User) = UpdateUserResponse(userService.updateUser(id, user))
}
