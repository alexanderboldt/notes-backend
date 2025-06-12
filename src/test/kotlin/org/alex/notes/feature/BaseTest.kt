package org.alex.notes.feature

import org.alex.notes.configuration.configureKoin
import org.alex.notes.configuration.configureLogging
import org.alex.notes.configuration.configureRouting
import org.alex.notes.configuration.configureSerialization
import org.alex.notes.repository.NoteTable
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.TestApplication
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.containers.MySQLContainer

open class BaseTest {

    protected val client = TestApplication {
        application {
            // configure the database
            val mysqlContainer = MySQLContainer("mysql:9.2.0").apply {
                withDatabaseName("notes")
                withUsername("admin")
                withPassword("admin")
                start()
            }

            val database = Database.connect(
                url = mysqlContainer.jdbcUrl,
                user = mysqlContainer.username,
                password = mysqlContainer.password,
                driver = mysqlContainer.driverClassName
            )

            transaction(database) {
                SchemaUtils.create(NoteTable)
            }

            configureSerialization()
            configureLogging()
            configureRouting()
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
}
