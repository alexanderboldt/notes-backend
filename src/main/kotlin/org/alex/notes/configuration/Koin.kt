package org.alex.notes.configuration

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.net.Host
import aws.smithy.kotlin.runtime.net.Scheme
import aws.smithy.kotlin.runtime.net.url.Url
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
    val profile = property<String>("ktor.profile")

    val bucketName = property<String>("s3.bucket")
    val region = property<String>("s3.region")

    install(Koin) {
        modules(
            module {
                if (profile == "prod") {
                    factory { S3Client { this.region = region } }
                } else {
                    val accessKey = property<String>("s3.accessKey")
                    val secretKey = property<String>("s3.secretKey")
                    val host = property<String>("s3.host")
                    val port = property<Int>("s3.port")

                    factory {
                        S3Client {
                            endpointUrl = Url {
                                this.scheme = Scheme.parse("http")
                                this.host = Host.parse(host)
                                this.port = port
                            }
                            this.region = region
                            credentialsProvider = StaticCredentialsProvider {
                                accessKeyId = accessKey
                                secretAccessKey = secretKey
                            }
                            forcePathStyle = true
                        }
                    }
                }
            },
            module { factory { S3Service(get(), bucketName) } },
            module { factoryOf(::NoteService) },
            module { factoryOf(::NoteImageService) },
            module { factoryOf(::NoteDao) },
        )
    }
}
