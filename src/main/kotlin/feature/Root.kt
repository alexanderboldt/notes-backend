package com.alex.feature

import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.rootRouting() {
    get("api") {
        call.respond("Welcome to the notes-backend.")
    }
}