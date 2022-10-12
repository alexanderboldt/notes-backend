package com.alex.main.kotlin.feature

import io.ktor.server.routing.*

fun Route.userRouting() {

    val pathV1 = "v1"

    val login = "login"

    route(pathV1) {
        route(login) {

        }
    }
}