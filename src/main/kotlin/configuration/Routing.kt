package com.alex.configuration

import com.alex.domain.Error
import com.alex.feature.notesRouting
import com.alex.feature.rootRouting
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
        rootRouting()
        notesRouting()
    }
}