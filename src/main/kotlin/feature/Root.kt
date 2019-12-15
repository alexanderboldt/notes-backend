package com.alex.main.kotlin.feature

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

object Root {
    fun routing(): Routing.() -> Unit = {
        get("/") {
            call.respond("welcome")
        }
    }
}