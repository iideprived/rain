package routes

import com.iideprived.rain.core.model.User
import com.iideprived.rain.core.routes.UserRouter
import com.iideprived.rain.core.service.UserService
import com.iideprived.rain.core.service.impl.UserServiceImpl
import kotlin.test.Test

internal class UserRouterTest {
    private val userService: UserService = UserServiceImpl()
    private val userRouter = UserRouter(userService)

    @Test
    fun testGetAllUsers() {
        // Assuming getAllUsers does not throw an exception
        userRouter.getAllUsers()
    }

    @Test
    fun testGetUserByIdValid() {
        // Assuming 1 is a valid ID
        userRouter.getUserById(1)
    }

    @Test
    fun testGetUserByIdInvalid() {
        // Assuming -1 is an invalid ID
        userRouter.getUserById(-1)
    }

    @Test
    fun testDeleteUserByIdValid() {
        // Assuming 1 is a valid ID
        userRouter.deleteUserById(1)
    }

    @Test
    fun testDeleteUserByIdInvalid() {
        // Assuming -1 is an invalid ID
        userRouter.deleteUserById(-1)
    }

    @Test
    fun testCreateUser() {
        val user = User(id = 1, name = "Test User")
        userRouter.createUser(user)
    }

    @Test
    fun testUpdateUserByIdValid() {
        val user = User(id = 1, name = "Updated User")
        // Assuming 1 is a valid ID
        userRouter.updateUserById(1, user)
    }

    @Test
    fun testUpdateUserByIdInvalid() {
        val user = User(id = -1, name = "Updated User")
        // Assuming -1 is an invalid ID
        userRouter.updateUserById(-1, user)    }
}