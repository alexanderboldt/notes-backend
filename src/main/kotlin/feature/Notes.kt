package com.alex.feature

import com.alex.domain.Note
import com.alex.repository.NoteDao
import com.alex.repository.toDomain
import com.alex.repository.toEntity
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*
import com.alex.utils.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.*
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
            call.respond(noteDao.getAll(call.offsetParameter, call.limitParameter).map { it.toDomain() })
        }

        get("/{id}") {
            val note = noteDao
                .get(call.idOrThrow)
                ?.toDomain()
                ?: throw BadRequestException(ErrorMessages.INVALID_ID)

            call.respond(note)
        }

        // update

        put("/{id}") {
            val note = call.receiveOrThrow<Note>()
            val id = call.idOrThrow

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
            if (!noteDao.delete(call.idOrThrow)) throw BadRequestException(ErrorMessages.INVALID_ID)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}