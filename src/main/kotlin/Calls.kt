package com.alex

import com.alex.repository.rest.RestModelError
import com.alex.utils.clientSecretHeader
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.response.respond

fun Application.configureCalls() {
    intercept(ApplicationCallPipeline.Call) {
        val clientSecret = "e4bbe5b7a4c1eb55652965aee885dd59bd2ee7f4"
        if (call.clientSecretHeader != clientSecret) {
            call.respond(HttpStatusCode.Unauthorized, RestModelError("Wrong or missing client-secret"))
        }
    }
}