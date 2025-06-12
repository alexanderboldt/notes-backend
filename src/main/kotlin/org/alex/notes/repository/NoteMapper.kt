package org.alex.notes.repository

import org.alex.notes.domain.Note
import java.util.Date

// from domain to entity

fun Note.toEntity() = NoteEntity(
    title = title,
    description = description,
    createdAt = Date().time,
    updatedAt = Date().time
)

// from entity to domain

fun NoteEntity.toDomain() = Note(id, title, description, createdAt, updatedAt)
