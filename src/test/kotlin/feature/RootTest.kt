package com.alex.feature

import feature.Routes
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotBlank
import kotlin.test.Test

class RootTest {

    @Test
    fun `should throw unauthorized with missing client-secret`() = testApplication {
        configureEnvironment()

        val response = client.get(Routes.ROOT)

        expectThat(response.status).isEqualTo(HttpStatusCode.Companion.Unauthorized)
    }

    @Test
    fun `should return valid response with client-secret`() = testApplication {
        configureEnvironment()

        val response = client.get(Routes.ROOT) {
            header("Client-Secret", "e4bbe5b7a4c1eb55652965aee885dd59bd2ee7f4")
        }

        expectThat(response.status).isEqualTo(HttpStatusCode.Companion.OK)
        expectThat(response.bodyAsText()).isNotBlank()
    }

    private fun ApplicationTestBuilder.configureEnvironment() {
        environment {
            this.config = ApplicationConfig("application.yaml")
        }
    }
}