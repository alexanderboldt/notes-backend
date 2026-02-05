package org.alex.notes.repository

import org.alex.notes.utils.Sort
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import java.util.Date

class NoteDao {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        suspendTransaction { block() }

    private fun getSingleNoteOrNull(id: Int): NoteEntity? {
        return NoteTable
            .selectAll()
            .where { NoteTable.id eq id }
            .map { it.toEntity() }
            .singleOrNull()
    }

    private fun ResultRow.toEntity() = NoteEntity(
        id = this[NoteTable.id],
        title = this[NoteTable.title],
        description = this[NoteTable.description],
        filename = this[NoteTable.filename],
        createdAt = this[NoteTable.createdAt],
        updatedAt = this[NoteTable.updatedAt]
    )

    // create

    suspend fun save(note: NoteEntity): NoteEntity = dbQuery {
        val inserted = NoteTable.insert {
            it[title] = note.title
            it[description] = note.description
            it[filename] = note.filename
            it[createdAt] = note.createdAt
            it[updatedAt] = note.updatedAt
        }

        getSingleNoteOrNull(inserted[NoteTable.id])!!
    }

    // read

    @Suppress("SpreadOperator")
    suspend fun getAll(sort: Sort?): List<NoteEntity> = dbQuery {
        NoteTable
            .selectAll()
            .apply { if (sort != null) orderBy(*sort.toTypedArray()) }
            .map { it.toEntity() }
    }

    suspend fun get(id: Int): NoteEntity? = dbQuery {
        getSingleNoteOrNull(id)
    }

    // update

    suspend fun update(note: NoteEntity): NoteEntity? = dbQuery {
        val couldUpdateNote = NoteTable.update(
            where = { NoteTable.id eq note.id },
            body = {
                it[title] = note.title
                it[description] = note.description
                it[updatedAt] = Date().time
            }) > 0

        when (couldUpdateNote) {
            true -> getSingleNoteOrNull(note.id)
            false -> null
        }
    }

    suspend fun updateFilename(id: Int, filenameNew: String): NoteEntity? = dbQuery {
        val couldUpdateNote = NoteTable.update(
            where = { NoteTable.id eq id },
            body = {
                it[filename] = filenameNew
                it[updatedAt] = Date().time
            }) > 0

        when (couldUpdateNote) {
            true -> getSingleNoteOrNull(id)
            false -> null
        }
    }

    suspend fun deleteFilename(id: Int): NoteEntity? = dbQuery {
        val couldUpdateNote = NoteTable.update(
            where = { NoteTable.id eq id },
            body = {
                it[filename] = null
                it[updatedAt] = Date().time
            }) > 0

        when (couldUpdateNote) {
            true -> getSingleNoteOrNull(id)
            false -> null
        }
    }

    // delete

    suspend fun delete(id: Int): Boolean = dbQuery {
        NoteTable.deleteWhere { NoteTable.id eq id } > 0
    }

    suspend fun deleteAll() = dbQuery {
        NoteTable.deleteAll()
    }
}
