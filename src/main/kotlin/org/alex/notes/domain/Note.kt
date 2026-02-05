package org.alex.notes.domain

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val title: String,
    val description: String?
)

@Serializable
data class NoteResponse(
    val id: Int = 0,
    val title: String,
    val description: String?,
    val filename: String?,
    val createdAt: Long,
    val updatedAt: Long
)
