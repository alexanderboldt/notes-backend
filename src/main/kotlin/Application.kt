package com.alex

import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureDatabase()
    configureCalls()
    configureSerialization()
    configureLogging()
    configureRouting()
}