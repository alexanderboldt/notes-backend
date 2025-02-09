package com.alex.repository.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NoteDao {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun ResultRow.toDbModel(): DbModelNote {
        return DbModelNote(
            id = this[NoteTable.id],
            title = this[NoteTable.title],
            description = this[NoteTable.description],
            createdAt = this[NoteTable.createdAt],
            updatedAt = this[NoteTable.updatedAt]
        )
    }

    // create

    suspend fun save(note: DbModelNote): DbModelNote = dbQuery {
        val inserted = NoteTable.insert {
            it[title] = note.title
            it[description] = note.description
            it[createdAt] = note.createdAt
            it[updatedAt] = note.updatedAt
        }

        NoteTable
            .selectAll()
            .where { NoteTable.id eq inserted[NoteTable.id] }
            .map { it.toDbModel() }
            .single()
    }

    // read

    suspend fun getAll(offset: Long?, limit: Int? = null): List<DbModelNote> = dbQuery {
        NoteTable
            .selectAll()
            .offset(offset ?: 0)
            .limit(limit ?: 0)
            .map { it.toDbModel() }
    }

    suspend fun get(id: Int): DbModelNote? = dbQuery {
        NoteTable
            .selectAll()
            .where { NoteTable.id eq id }
            .map { it.toDbModel() }
            .singleOrNull()
    }

    // update

    suspend fun update(note: DbModelNote): Boolean = dbQuery {
        NoteTable.update(
            where = { NoteTable.id eq note.id },
            body = {
                it[title] = note.title
                it[description] = note.description
                it[updatedAt] = note.updatedAt
            }) > 0
    }

    // delete

    suspend fun delete(id: Int): Boolean = dbQuery {
        NoteTable.deleteWhere { NoteTable.id eq id } > 0
    }
}