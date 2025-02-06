package com.alex.repository.rest

import kotlinx.serialization.Serializable

@Serializable
data class RestModelNotePost(
    val title: String,
    val description: String?
)