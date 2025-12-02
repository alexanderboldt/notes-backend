package org.alex.notes.configuration

import org.alex.notes.repository.NoteDao
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.alex.notes.service.NoteService
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

val noteServiceModule = module {
    factoryOf(::NoteService)
}

val noteDaoModule = module {
    factoryOf(::NoteDao)
}

fun Application.configureKoin() {
    install(Koin) {
        modules(noteServiceModule, noteDaoModule)
    }
}
