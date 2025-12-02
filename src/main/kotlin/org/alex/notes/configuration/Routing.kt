package org.alex.notes.configuration

import org.alex.notes.feature.notesRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        notesRouting()
    }
}
