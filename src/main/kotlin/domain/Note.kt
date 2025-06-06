package com.alex.domain

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    var id: Int?,
    var title: String,
    var description: String?,
    var createdAt: Long?,
    var updatedAt: Long?
)