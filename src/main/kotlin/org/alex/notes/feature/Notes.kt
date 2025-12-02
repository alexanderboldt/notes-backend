package org.alex.notes.feature

import org.alex.notes.domain.NoteRequest
import org.alex.notes.repository.NoteTable
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
import org.alex.notes.service.NoteService
import org.alex.notes.utils.Path
import org.alex.notes.utils.PathParam
import org.alex.notes.utils.QueryParam
import org.alex.notes.utils.convertToSort
import org.koin.ktor.ext.inject

fun Route.noteRoutes() {
    route(Path.NOTES) {

        val noteService: NoteService by inject()

        // create

        post {
            val note = call.receive<NoteRequest>()
            call.respond(HttpStatusCode.Created, noteService.create(note))
        }

        // read

        get {
            val sort = call.queryParameters[QueryParam.SORT]?.convertToSort(NoteTable.columns)
            call.respond(noteService.readAll(sort))
        }

        get(Path.ID) {
            val id = call.pathParameters.getOrFail<Int>(PathParam.ID)
            call.respond(noteService.read(id))
        }

        // update

        put(Path.ID) {
            val id = call.pathParameters.getOrFail<Int>(PathParam.ID)
            val note = call.receive<NoteRequest>()

            call.respond(noteService.update(id, note))
        }

        // delete

        delete(Path.ID) {
            val id = call.pathParameters.getOrFail<Int>(PathParam.ID)
            noteService.delete(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
