package com.alex

import com.alex.configuration.configureCalls
import com.alex.configuration.configureDatabase
import com.alex.configuration.configureKoin
import com.alex.configuration.configureLogging
import com.alex.configuration.configureRouting
import com.alex.configuration.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module(testing: Boolean = false) {
    configureDatabase()
    configureCalls()
    configureSerialization()
    configureLogging()
    configureRouting()
    configureKoin()
}