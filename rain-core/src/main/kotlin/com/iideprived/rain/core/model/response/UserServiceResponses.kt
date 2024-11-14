package com.iideprived.rain.core.model.response

import com.iideprived.rain.core.model.User
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateUserResponse(val user: User? = null) : BaseResponse()

@Serializable
internal class DeleteUserResponse: BaseResponse()

@Serializable
internal data class GetAllUsersResponse(val users: List<User>? = null) : BaseResponse()

@Serializable
internal data class GetUserByIdResponse(val user: User? = null) : BaseResponse()

@Serializable
internal data class UpdateUserResponse(val user: User? =  null) : BaseResponse()