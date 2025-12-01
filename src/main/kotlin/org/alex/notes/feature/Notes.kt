package org.alex.notes.feature

import org.alex.notes.domain.NoteRequest
import org.alex.notes.repository.NoteDao
import org.alex.notes.repository.NoteTable
import org.alex.notes.repository.toDomain
import org.alex.notes.repository.toEntity
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import org.alex.notes.utils.ErrorMessages
import org.alex.notes.utils.convertToSort
import org.koin.ktor.ext.inject

fun Route.notesRouting() {

    route("api/v1/notes") {

        val noteDao: NoteDao by inject()

        // create

        post {
            val note = call.receive<NoteRequest>().toEntity()

            call.respond(HttpStatusCode.Created, noteDao.save(note).toDomain())
        }

        // read

        get {
            val sort = call.queryParameters["sort"]?.convertToSort(NoteTable.columns)

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
            val note = call.receive<NoteRequest>()
            val id = call.pathParameters.getOrFail<Int>("id")

            val noteUpdated = noteDao
                .update(note.toEntity().copy(id = id))
                ?.toDomain()
                ?: throw BadRequestException(ErrorMessages.UPDATE_FAILED)

            call.respond(noteUpdated)
        }

        // delete

        delete("/{id}") {
            if (!noteDao.delete(call.pathParameters.getOrFail<Int>("id"))) {
                throw BadRequestException(ErrorMessages.INVALID_ID)
            }

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
