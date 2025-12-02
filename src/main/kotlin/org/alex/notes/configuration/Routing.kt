package org.alex.notes.configuration

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.alex.notes.feature.noteRoutes
import org.alex.notes.utils.Path

fun Application.configureRouting() {
    routing {
        route(Path.API) {
            noteRoutes()
        }
    }
}
