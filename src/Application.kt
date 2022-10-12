package com.alex

import com.alex.main.kotlin.feature.notesRouting
import com.alex.main.kotlin.feature.rootRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // todo: implement an api-key for more security
    // call.request.headers["Api-Key"] = "RcUaL2324juXiR6OW03hX478LXFizFKjCndeunNB"

    install(ContentNegotiation) {
        json(
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        )
    }

    install(CallLogging) {
        level = Level.INFO
    }

    routing() {
        rootRouting()
        notesRouting()
    }
}