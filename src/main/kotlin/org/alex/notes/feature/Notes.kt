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
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import org.alex.notes.utils.BadRequestThrowable
import org.alex.notes.utils.Path
import org.alex.notes.utils.PathParam
import org.alex.notes.utils.QueryParam
import org.alex.notes.utils.convertToSort
import org.koin.ktor.ext.inject

fun Route.noteRoutes() {
    route(Path.NOTES) {

        val noteDao: NoteDao by inject()

        // create

        post {
            val note = call.receive<NoteRequest>().toEntity()

            call.respond(HttpStatusCode.Created, noteDao.save(note).toDomain())
        }

        // read

        get {
            val sort = call.queryParameters[QueryParam.SORT]?.convertToSort(NoteTable.columns)

            call.respond(noteDao.getAll(sort).map { it.toDomain() })
        }

        get(Path.ID) {
            val note = noteDao
                .get(call.pathParameters.getOrFail<Int>(PathParam.ID))
                ?.toDomain()
                ?: throw BadRequestThrowable()

            call.respond(note)
        }

        // update

        put(Path.ID) {
            val note = call.receive<NoteRequest>()
            val id = call.pathParameters.getOrFail<Int>(PathParam.ID)

            val noteUpdated = noteDao
                .update(note.toEntity().copy(id = id))
                ?.toDomain()
                ?: throw BadRequestThrowable()

            call.respond(noteUpdated)
        }

        // delete

        delete(Path.ID) {
            if (!noteDao.delete(call.pathParameters.getOrFail<Int>(PathParam.ID))) {
                throw BadRequestThrowable()
            }

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
