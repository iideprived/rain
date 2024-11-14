package com.iideprived.rain.core.model

import kotlinx.serialization.Serializable

@Serializable
internal data class User(
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null
)
