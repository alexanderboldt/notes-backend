package com.alex

import com.alex.repository.rest.RestModelError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.response.respond

fun Application.configureCalls() {
    val clientSecret = environment.config.property("jwt.secret").getString()

    intercept(ApplicationCallPipeline.Call) {
        if (call.request.headers["Client-Secret"] != clientSecret) {
            call.respond(HttpStatusCode.Unauthorized, RestModelError("Wrong or missing client-secret"))
        }
    }
}