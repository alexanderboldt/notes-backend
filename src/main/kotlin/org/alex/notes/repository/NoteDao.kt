package org.alex.notes.repository

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteAll
import java.util.Date

class NoteDao {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

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
        createdAt = this[NoteTable.createdAt],
        updatedAt = this[NoteTable.updatedAt]
    )

    // create

    suspend fun save(note: NoteEntity): NoteEntity = dbQuery {
        val inserted = NoteTable.insert {
            it[title] = note.title
            it[description] = note.description
            it[createdAt] = note.createdAt
            it[updatedAt] = note.updatedAt
        }

        getSingleNoteOrNull(inserted[NoteTable.id])!!
    }

    // read

    @Suppress("SpreadOperator") // orderBy requires vararg
    suspend fun getAll(sort: List<Pair<Column<*>, SortOrder>>?): List<NoteEntity> = dbQuery {
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

    // delete

    suspend fun delete(id: Int): Boolean = dbQuery {
        NoteTable.deleteWhere { NoteTable.id eq id } > 0
    }

    suspend fun deleteAll() = dbQuery {
        NoteTable.deleteAll()
    }
}
