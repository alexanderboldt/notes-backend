package com.alex

import com.alex.feature.notesRouting
import com.alex.feature.rootRouting
import com.alex.repository.rest.RestModelError
import com.alex.utils.clientSecretHeader
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {

    intercept(ApplicationCallPipeline.Call) {
        val clientSecret = "e4bbe5b7a4c1eb55652965aee885dd59bd2ee7f4"
        if (call.clientSecretHeader != clientSecret) {
            call.respond(HttpStatusCode.Unauthorized, RestModelError("Wrong or missing client-secret"))
        }
    }

    install(ContentNegotiation) {
        json(
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        )
    }

    install(CallLogging) {
        level = Level.INFO
    }

    routing() {
        rootRouting()
        notesRouting()
    }
}