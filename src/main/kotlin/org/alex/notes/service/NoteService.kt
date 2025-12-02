package org.alex.notes.service

import org.alex.notes.domain.NoteRequest
import org.alex.notes.domain.NoteResponse
import org.alex.notes.repository.NoteDao
import org.alex.notes.repository.toDomain
import org.alex.notes.repository.toEntity
import org.alex.notes.utils.BadRequestThrowable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder

class NoteService(private val noteDao: NoteDao) {

    // create

    suspend fun create(note: NoteRequest): NoteResponse {
        return noteDao.save(note.toEntity()).toDomain()
    }

    // read

    suspend fun readAll(sort: List<Pair<Column<*>, SortOrder>>?): List<NoteResponse> {
        return noteDao.getAll(sort).map { it.toDomain() }
    }

    suspend fun read(id: Int): NoteResponse {
        return noteDao
            .get(id)
            ?.toDomain()
            ?: throw BadRequestThrowable()
    }

    // update

    suspend fun update(id: Int, noteUpdate: NoteRequest): NoteResponse {
        return noteDao
            .update(noteUpdate.toEntity().copy(id = id))
            ?.toDomain()
            ?: throw BadRequestThrowable()
    }

    // delete

    suspend fun delete(id: Int) {
        if (!noteDao.delete(id)) {
            throw BadRequestThrowable()
        }
    }
}
