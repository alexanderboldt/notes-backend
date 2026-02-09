package org.alex.notes.configuration

import org.alex.notes.repository.NoteDao
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.property
import org.alex.notes.service.NoteImageService
import org.alex.notes.service.NoteService
import org.alex.notes.service.S3Service
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    val accessKey = property<String>("s3.accessKey")
    val secretKey = property<String>("s3.secretKey")
    val host = property<String>("s3.host")
    val port = property<Int>("s3.port")
    val region = property<String>("s3.region")

    install(Koin) {
        modules(
            module {
                factory {
                    S3Service(
                        host,
                        port,
                        region,
                        accessKey,
                        secretKey
                    )
                }
            },
            module { factoryOf(::NoteService) },
            module { factoryOf(::NoteImageService) },
            module { factoryOf(::NoteDao) },
        )
    }
}
