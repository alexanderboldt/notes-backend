package org.alex.notes.configuration

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import java.net.URL
import java.util.concurrent.TimeUnit

const val AUTHENTICATION_JWT = "auth-jwt"

fun Application.configureAuthentication() {
    val issuer = environment.config.property("jwt.issuer-uri").getString()

    install(Authentication) {
        jwt(AUTHENTICATION_JWT) {
            val jwkProvider = JwkProviderBuilder(URL("$issuer/protocol/openid-connect/certs"))
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build()

            verifier(jwkProvider, issuer)
            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }
}