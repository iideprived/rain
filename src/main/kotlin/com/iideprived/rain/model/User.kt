package com.iideprived.rain.model

import kotlinx.serialization.Serializable

@Serializable
internal data class User(
    val id: Int,
    val name: String,
    val email: String
)
