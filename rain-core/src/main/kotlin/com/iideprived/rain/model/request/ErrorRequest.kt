package com.iideprived.rain.model.request

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorRequest(
    val hasStatus: Boolean = false,
    val hasCode: Boolean = false
)