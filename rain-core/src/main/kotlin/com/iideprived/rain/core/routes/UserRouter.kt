package com.iideprived.rain.core.routes

import com.iideprived.rain.core.annotations.routing.Service
import com.iideprived.rain.core.annotations.routing.httpmethod.Delete
import com.iideprived.rain.core.annotations.routing.httpmethod.Get
import com.iideprived.rain.core.annotations.routing.httpmethod.Post
import com.iideprived.rain.core.annotations.routing.httpmethod.Put
import com.iideprived.rain.core.annotations.routing.request.Body
import com.iideprived.rain.core.annotations.routing.request.Path
import com.iideprived.rain.core.model.User
import com.iideprived.rain.core.model.response.*
import com.iideprived.rain.core.service.UserService
import com.iideprived.rain.core.service.impl.UserServiceImpl

@Service("/users")
internal class UserRouter(private val userService: UserService = UserServiceImpl()) {

    @Get
    internal fun getAllUsers(): GetAllUsersResponse = GetAllUsersResponse(userService.getAllUsers())

    @Get("/{id}")
    internal fun getUserById(@Path("id") id: Int) = when (val user = userService.getUserById(id)) {
        null -> GetUserByIdResponse().asFailure(Exception("User not found"), "USER_NOT_FOUND", 404)
        else -> GetUserByIdResponse(user)
    }

    @Delete("/{id}")
    internal fun deleteUserById(@Path("id") id: Int) = if (userService.deleteUser(id)){
        DeleteUserResponse()
    } else {
        DeleteUserResponse().asFailure(Exception("Failed to delete user"), "DELETE_FAILED", 403)
    }

    @Post
    internal fun createUser(@Body user: User) = CreateUserResponse(userService.createUser(user))

    @Put("/{id}")
    internal fun updateUserById(@Path("id") id: Int, @Body user: User) = UpdateUserResponse(userService.updateUser(id, user))


}
