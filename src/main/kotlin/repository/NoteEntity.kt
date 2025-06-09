package com.alex.repository

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class NoteEntity(
    var id: Int = 0,
    var title: String,
    var description: String?,
    var createdAt: Long,
    var updatedAt: Long
)

object NoteTable : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 50)
    val description = varchar("description", 50).nullable()
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}