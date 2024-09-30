package com.iideprived.rain.model

import kotlinx.serialization.Serializable

@Serializable
internal data class User(
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null
)
