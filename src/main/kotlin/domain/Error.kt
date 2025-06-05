package com.alex.domain

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val status: Int,
    val message: String
)
