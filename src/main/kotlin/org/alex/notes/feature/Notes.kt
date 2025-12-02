package org.alex.notes.feature

import org.alex.notes.repository.NoteTable
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.route
import org.alex.notes.domain.NoteRequest
import org.alex.notes.service.NoteService
import org.alex.notes.utils.Path
import org.alex.notes.utils.getSort
import org.alex.notes.utils.id
import org.koin.ktor.ext.inject

suspend fun RoutingContext.getNote() = call.receive<NoteRequest>()

fun Route.noteRoutes() {
    route(Path.NOTES) {
        val noteService: NoteService by inject()

        // create

        post {
            call.respond(HttpStatusCode.Created, noteService.create(getNote()))
        }

        // read

        get {
            call.respond(noteService.readAll(getSort(NoteTable.columns)))
        }

        get(Path.ID) {
            call.respond(noteService.read(id))
        }

        // update

        put(Path.ID) {
            call.respond(noteService.update(id, getNote()))
        }

        // delete

        delete(Path.ID) {
            noteService.delete(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
