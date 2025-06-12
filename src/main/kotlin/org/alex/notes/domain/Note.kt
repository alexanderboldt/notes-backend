package org.alex.notes.domain

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int = 0,
    val title: String,
    val description: String?,
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)
