package com.alex.main.kotlin.repository.rest

import kotlinx.serialization.Serializable

@Serializable
data class RestModelNoteGet(
    var id: Int,
    var title: String,
    var description: String?,
    var createdAt: Long,
    var updatedAt: Long
)
