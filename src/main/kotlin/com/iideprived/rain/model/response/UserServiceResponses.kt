package com.iideprived.rain.model.response

import com.iideprived.rain.model.User
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateUserResponse(val user: User? = null) : BaseResponse<CreateUserResponse>()

@Serializable
internal class DeleteUserResponse: BaseResponse<DeleteUserResponse>()

@Serializable
internal data class GetAllUsersResponse(val users: List<User>? = null) : BaseResponse<GetAllUsersResponse>()

@Serializable
internal data class GetUserByIdResponse(val user: User? = null) : BaseResponse<GetUserByIdResponse>()

@Serializable
internal data class UpdateUserResponse(val user: User? =  null) : BaseResponse<UpdateUserResponse>()