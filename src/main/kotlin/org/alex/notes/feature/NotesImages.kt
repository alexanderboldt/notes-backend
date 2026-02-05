package org.alex.notes.feature

import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.post
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.alex.notes.service.NoteImageService
import org.alex.notes.utils.Path
import org.alex.notes.utils.id
import org.koin.ktor.ext.inject

fun Route.noteImageRoutes() {
    route(Path.NOTES_IMAGES) {
        val noteImageService: NoteImageService by inject()

        post {
            val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100)

            call.respond(noteImageService.uploadImage(id, multipartData))
        }

        get {
            val (stream, filename) = noteImageService.downloadImage(id)

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment
                    .withParameter(ContentDisposition.Parameters.FileName, filename)
                    .toString()
            )

            call.respondBytes(stream.readBytes())
        }

        delete {
            noteImageService.deleteImage(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
