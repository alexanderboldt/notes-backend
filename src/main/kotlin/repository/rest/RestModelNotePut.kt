package com.alex.repository.rest

import kotlinx.serialization.Serializable

@Serializable
data class RestModelNotePut(
    val title: String?,
    val description: String?
)
