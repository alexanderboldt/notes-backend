package com.alex.main.kotlin.repository

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Note(var id: Int?, var title: String, var description: String?)