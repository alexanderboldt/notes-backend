package com.alex.configuration

import com.alex.repository.database.NoteDao
import io.ktor.server.application.Application
import org.koin.dsl.module
import org.koin.ktor.plugin.koin

val noteDaoModule = module {
    single { NoteDao() }
}

fun Application.configureKoin() {
    koin {
        modules(noteDaoModule)
    }
}