package org.alex.notes.configuration

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import org.alex.notes.domain.Error

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest,
                Error(HttpStatusCode.BadRequest.value, cause.message ?: HttpStatusCode.BadRequest.description)
            )
        }
    }
}
