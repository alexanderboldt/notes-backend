package com.alex.main.kotlin.repository.rest

import kotlinx.serialization.Serializable

@Serializable
data class RestModelNotePost(
    val title: String,
    val description: String?
)