package org.alex.notes

import io.ktor.server.application.Application
import org.alex.notes.configuration.configureAuthentication
import org.alex.notes.configuration.configureCalls
import org.alex.notes.configuration.configureDatabase
import org.alex.notes.configuration.configureKoin
import org.alex.notes.configuration.configureLogging
import org.alex.notes.configuration.configureRouting
import org.alex.notes.configuration.configureSerialization
import org.alex.notes.configuration.configureStatusPages

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module(testing: Boolean = false) {
    configureDatabase()
    configureCalls()
    configureSerialization()
    configureLogging()
    configureAuthentication()
    configureStatusPages()
    configureRouting()
    configureKoin()
}
