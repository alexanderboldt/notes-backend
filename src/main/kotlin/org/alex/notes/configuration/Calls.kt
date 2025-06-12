package org.alex.notes.configuration

import org.alex.notes.domain.Error
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.response.respond

fun Application.configureCalls() {
    val clientSecret = environment.config.property("jwt.secret").getString()

    intercept(ApplicationCallPipeline.Call) {
        if (call.request.headers["Client-Secret"] != clientSecret) {
            call.respond(HttpStatusCode.Unauthorized,
                Error(HttpStatusCode.Unauthorized.value, "Wrong or missing client-secret")
            )
        }
    }
}
