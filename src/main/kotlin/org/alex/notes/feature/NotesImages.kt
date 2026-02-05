package org.alex.notes.feature

import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.alex.notes.service.NoteImageService
import org.alex.notes.utils.Path
import org.koin.ktor.ext.inject

fun Route.noteImageRoutes() {
    route(Path.NOTES_IMAGES) {
        val noteImageService: NoteImageService by inject()

        // create

        post {
            val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100)

            call.respond(noteImageService.uploadImage(multipartData))
        }
    }
}
