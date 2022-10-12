package com.alex.main.kotlin.repository.rest

import kotlinx.serialization.Serializable

@Serializable
data class RestModelNotePut(
    val title: String?,
    val description: String?
)
