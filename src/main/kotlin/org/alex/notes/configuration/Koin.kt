package org.alex.notes.configuration

import org.alex.notes.repository.NoteDao
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

val noteDaoModule = module {
    singleOf(::NoteDao)
}

fun Application.configureKoin() {
    install(Koin) {
        modules(noteDaoModule)
    }
}
