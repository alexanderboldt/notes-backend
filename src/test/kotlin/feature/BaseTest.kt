package feature

import com.alex.configuration.configureKoin
import com.alex.configuration.configureLogging
import com.alex.configuration.configureRouting
import com.alex.configuration.configureSerialization
import com.alex.repository.NoteTable
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.TestApplication
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
            json()
        }
    }
}