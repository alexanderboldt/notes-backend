package org.alex.notes.configuration

import org.alex.notes.domain.Error
import org.alex.notes.feature.notesRouting
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest,
                Error(HttpStatusCode.BadRequest.value, cause.message ?: HttpStatusCode.BadRequest.description)
            )
        }
    }
    routing {
        notesRouting()
    }
}
