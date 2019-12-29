package com.alex.main.kotlin.repository

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotesList(val list: List<Note>)