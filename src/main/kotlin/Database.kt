package com.alex

import com.alex.repository.database.NoteTable
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/notes",
        user = "admin",
        password = "admin",
        driver = "com.mysql.cj.jdbc.Driver"
    )

    transaction(database) {
        SchemaUtils.create(NoteTable)
    }
}