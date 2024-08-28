package com.iideprived.rain.routes

import com.iideprived.rain.annotations.*
import com.iideprived.rain.model.User
import com.iideprived.rain.model.response.*
import com.iideprived.rain.service.UserService

@Service("/users")
internal class UserRouter(private val userService: UserService) {

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
