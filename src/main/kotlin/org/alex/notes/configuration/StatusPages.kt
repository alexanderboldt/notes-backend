package org.alex.notes.configuration

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import org.alex.notes.domain.Error
import org.alex.notes.utils.BadRequestThrowable

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<BadRequestThrowable> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, Error(cause.message ?: HttpStatusCode.BadRequest.description))
        }
    }
}
