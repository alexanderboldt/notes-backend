package org.alex.notes.feature

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.TestApplication
import kotlinx.serialization.json.Json
import org.alex.notes.configuration.configureKoin
import org.alex.notes.configuration.configureSerialization
import org.alex.notes.configuration.configureTestDatabase
import org.alex.notes.configuration.configureTestRouting

val client = TestApplication {
    application {
        configureTestDatabase()
        configureSerialization()
        configureTestRouting()
        configureKoin()
    }
}.createClient {
    install(ContentNegotiation) {
        json(
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        )
    }

    defaultRequest {
        contentType(ContentType.Application.Json)
    }
}
