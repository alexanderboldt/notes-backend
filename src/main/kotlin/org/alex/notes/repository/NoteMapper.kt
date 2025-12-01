package org.alex.notes.repository

import org.alex.notes.domain.NoteRequest
import org.alex.notes.domain.NoteResponse
import java.util.Date

// from domain to entity

fun NoteRequest.toEntity() = NoteEntity(
    title = title,
    description = description,
    createdAt = Date().time,
    updatedAt = Date().time
)

// from entity to domain

fun NoteEntity.toDomain() = NoteResponse(id, title, description, createdAt, updatedAt)
