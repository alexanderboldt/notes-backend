package com.alex.main.kotlin.repository.database

data class DbModelNote(
    var id: Int,
    var title: String,
    var description: String?,
    var createdAt: Long,
    var updatedAt: Long
)