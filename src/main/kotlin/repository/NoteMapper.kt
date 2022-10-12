package com.alex.main.kotlin.repository

import com.alex.main.kotlin.repository.database.DbModelNote
import com.alex.main.kotlin.repository.rest.RestModelNoteGet

fun List<DbModelNote>.toRestModelGet(): List<RestModelNoteGet> {
    return map {
        RestModelNoteGet(
            it.id,
            it.title,
            it.description,
            it.createdAt,
            it.updatedAt
        )
    }
}

fun DbModelNote.toRestModelGet(): RestModelNoteGet {
    return RestModelNoteGet(
        id,
        title,
        description,
        createdAt,
        updatedAt
    )
}