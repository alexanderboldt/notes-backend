package org.alex.notes.repository

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.Table

@Serializable
data class NoteEntity(
    var id: Int = 0,
    var title: String,
    var description: String?,
    var filename: String?,
    var createdAt: Long,
    var updatedAt: Long
)

@Suppress("MagicNumber")
object NoteTable : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 50)
    val description = varchar("description", 50).nullable()
    val filename = varchar("filename", 100).nullable()
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}
