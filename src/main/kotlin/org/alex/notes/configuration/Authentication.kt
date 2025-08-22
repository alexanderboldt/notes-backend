package org.alex.notes.configuration

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.property
import java.net.URL
import java.util.concurrent.TimeUnit

const val AUTHENTICATION_JWT = "auth-jwt"

fun Application.configureAuthentication() {
    val issuer = property<String>("jwt.issuer")

    install(Authentication) {
        jwt(AUTHENTICATION_JWT) {
            val jwkProvider = JwkProviderBuilder(URL(issuer))
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
