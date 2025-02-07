package com.alex

import com.alex.feature.notesRouting
import com.alex.feature.rootRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing() {
        rootRouting()
        notesRouting()
    }
}