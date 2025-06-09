package com.alex.feature

import com.alex.domain.Note
import com.alex.repository.NoteDao
import com.alex.repository.NoteTable
import com.alex.repository.toDomain
import com.alex.repository.toEntity
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*
import com.alex.utils.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.*
import io.ktor.server.util.getOrFail
import org.koin.ktor.ext.inject
import java.util.*

fun Route.notesRouting() {

    route("api/v1/notes") {

        val noteDao: NoteDao by inject()

        // create

        post {
            val note = call.receiveOrThrow<Note>().toEntity()
            call.respond(HttpStatusCode.Created, noteDao.save(note).toDomain())
        }

        // read

        get {
            val sort = call.sortParameter?.convertToSort(NoteTable.columns)
            call.respond(noteDao.getAll(sort).map { it.toDomain() })
        }

        get("/{id}") {
            val note = noteDao
                .get(call.pathParameters.getOrFail<Int>("id"))
                ?.toDomain()
                ?: throw BadRequestException(ErrorMessages.INVALID_ID)

            call.respond(note)
        }

        // update

        put("/{id}") {
            val note = call.receiveOrThrow<Note>()
            val id = call.pathParameters.getOrFail<Int>("id")

            noteDao
                .get(id)
                ?.apply {
                    title = note.title
                    description = note.description ?: description
                    updatedAt = Date().time
                }?.apply {
                    noteDao.update(this)
                    call.respond(noteDao.get(id)!!.toDomain())
                } ?: throw BadRequestException(ErrorMessages.UPDATE_FAILED)
        }

        // delete

        delete("/{id}") {
            if (!noteDao.delete(call.pathParameters.getOrFail<Int>("id"))) throw BadRequestException(ErrorMessages.INVALID_ID)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}