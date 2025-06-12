package org.alex.notes.configuration

import org.alex.notes.repository.NoteDao
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
