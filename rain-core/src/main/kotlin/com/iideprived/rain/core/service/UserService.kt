package com.iideprived.rain.core.service

import com.iideprived.rain.core.model.User

internal interface UserService {
    fun getAllUsers(): List<User>
    fun getUserById(id: Int): User?
    fun createUser(user: User): User
    fun updateUser(id: Int, user: User): User?
    fun deleteUser(id: Int): Boolean
}