package org.alex.notes.configuration

import io.ktor.server.application.Application
import org.alex.notes.repository.NoteTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.containers.MySQLContainer

fun Application.configureTestDatabase() {
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
}
