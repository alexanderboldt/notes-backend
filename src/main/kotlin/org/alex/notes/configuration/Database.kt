package org.alex.notes.configuration

import org.alex.notes.repository.NoteTable
import io.ktor.server.application.Application
import io.ktor.server.config.property
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

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
