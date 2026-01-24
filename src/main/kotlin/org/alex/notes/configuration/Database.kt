package org.alex.notes.configuration

import org.alex.notes.repository.NoteTable
import io.ktor.server.application.Application
import io.ktor.server.config.property
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

@Suppress("unused")
fun Application.configureDatabase() {
    val database = Database.connect(
        url = property<String>("database.url"),
        user = property<String>("database.user"),
        password = property<String>("database.password")
    )

    transaction(database) {
        SchemaUtils.create(NoteTable)
    }
}
