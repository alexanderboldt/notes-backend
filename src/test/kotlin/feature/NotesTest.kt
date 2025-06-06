package feature

import com.alex.configuration.configureKoin
import com.alex.configuration.configureLogging
import com.alex.configuration.configureRouting
import com.alex.configuration.configureSerialization
import com.alex.domain.Note
import com.alex.repository.NoteTable
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.containers.MySQLContainer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NotesTest {

    @Test
    fun `should create a note with valid request`() = testApplication {
        configureApplication()

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/api/v1/notes") {
            contentType(ContentType.Application.Json)
            setBody(Fixtures.Notes.Domain.dinner)
        }

        val note = response.body<Note>()

        assertEquals(HttpStatusCode.Created, response.status)
        assertNotNull(note.id)
        assertEquals(Fixtures.Notes.Domain.dinner.title, note.title)
        assertEquals(Fixtures.Notes.Domain.dinner.description, note.description)
        assertNotNull(note.createdAt)
        assertNotNull(note.updatedAt)
    }

    private fun ApplicationTestBuilder.configureApplication() {
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
    }
}