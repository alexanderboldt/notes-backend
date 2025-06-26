package org.alex.notes.configuration

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.alex.notes.feature.notesRouting

fun Application.configureTestRouting() {
    routing {
        notesRouting()
    }
}
