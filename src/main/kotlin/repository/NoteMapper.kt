package com.alex.repository

import com.alex.domain.Note
import java.util.Date

// from domain to entity

fun Note.toEntity() = NoteEntity(0, title, description, Date().time, Date().time)

// from entity to domain

fun NoteEntity.toDomain() = Note(id, title, description, createdAt, updatedAt)