package com.alex.repository

import com.alex.repository.database.DbModelNote
import com.alex.repository.rest.RestModelNoteGet
import com.alex.repository.rest.RestModelNotePost
import java.util.Date

// from rest to database

fun RestModelNotePost.toDbModel() = DbModelNote(0, title, description, Date().time, Date().time)

// from database to rest

fun DbModelNote.toRestModelGet(): RestModelNoteGet {
    return RestModelNoteGet(
        id,
        title,
        description,
        createdAt,
        updatedAt
    )
}