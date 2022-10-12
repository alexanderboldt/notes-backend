package com.alex.main.kotlin.feature

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.rootRouting() {
    get("/") {
        call.respond("Welcome to the notes-backend.")
    }
}