package com.iideprived.rain.service.impl

import com.iideprived.rain.model.User
import com.iideprived.rain.service.UserService

internal class UserServiceImpl : UserService {
    private val users = mutableListOf<User>()

    override fun getAllUsers(): List<User> {
        return users
    }

    override fun getUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    override fun createUser(user: User): User {
        return user.copy(id = users.size + 1).apply {
            users.add(this)
        }
    }

    override fun updateUser(id: Int, user: User): User? {
        val existingUserIndex = users.indexOfFirst { it.id == id }
        return if (existingUserIndex != -1) {
            users[existingUserIndex] = user
            user
        } else {
            null
        }
    }

    override fun deleteUser(id: Int): Boolean {
        return users.removeIf { it.id == id }
    }
}
