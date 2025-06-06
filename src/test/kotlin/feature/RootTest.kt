package com.alex.feature

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RootTest {

    @Test
    fun `should throw unauthorized with missing client-secret`() = testApplication {
        configureEnvironment()

        val response = client.get("/api")

        assertEquals(HttpStatusCode.Companion.Unauthorized, response.status)
    }

    @Test
    fun `should return valid response with client-secret`() = testApplication {
        configureEnvironment()

        val response = client.get("/api") {
            header("Client-Secret", "e4bbe5b7a4c1eb55652965aee885dd59bd2ee7f4")
        }

        assertEquals(HttpStatusCode.Companion.OK, response.status)
        assertNotNull(response.bodyAsText())
    }

    private fun ApplicationTestBuilder.configureEnvironment() {
        environment {
            this.config = ApplicationConfig("application.yaml")
        }
    }
}