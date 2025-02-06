package com.alex.feature

import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.rootRouting() {
    get("/") {
        call.respond("Welcome to the notes-backend.")
    }
}