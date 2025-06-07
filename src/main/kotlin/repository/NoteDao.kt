package com.alex.repository

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

    private fun ResultRow.toEntity(): NoteEntity {
        return NoteEntity(
            id = this[NoteTable.id],
            title = this[NoteTable.title],
            description = this[NoteTable.description],
            createdAt = this[NoteTable.createdAt],
            updatedAt = this[NoteTable.updatedAt]
        )
    }

    // create

    suspend fun save(note: NoteEntity): NoteEntity = dbQuery {
        val inserted = NoteTable.insert {
            it[title] = note.title
            it[description] = note.description
            it[createdAt] = note.createdAt
            it[updatedAt] = note.updatedAt
        }

        NoteTable
            .selectAll()
            .where { NoteTable.id eq inserted[NoteTable.id] }
            .map { it.toEntity() }
            .single()
    }

    // read

    suspend fun getAll(offset: Long?, limit: Int? = null): List<NoteEntity> = dbQuery {
        NoteTable
            .selectAll()
            //.offset(offset ?: 0)
            //.limit(limit ?: 0)
            .map { it.toEntity() }
    }

    suspend fun get(id: Int): NoteEntity? = dbQuery {
        NoteTable
            .selectAll()
            .where { NoteTable.id eq id }
            .map { it.toEntity() }
            .singleOrNull()
    }

    // update

    suspend fun update(note: NoteEntity): Boolean = dbQuery {
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