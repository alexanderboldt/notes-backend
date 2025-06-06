@file:Suppress("UnusedReceiverParameter")

package com.alex.configuration

import com.alex.repository.NoteTable
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("unused")
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