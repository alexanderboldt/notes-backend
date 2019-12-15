package com.alex

import com.alex.main.kotlin.feature.Root
import com.alex.main.kotlin.notes.Notes
import io.ktor.application.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing(Root.routing())
    routing(Notes.routing())
}