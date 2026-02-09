package org.alex.notes.service

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.createBucket
import aws.sdk.kotlin.services.s3.deleteObject
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.putObject
import aws.smithy.kotlin.runtime.content.asByteStream
import aws.smithy.kotlin.runtime.content.writeToFile
import aws.smithy.kotlin.runtime.net.Host
import aws.smithy.kotlin.runtime.net.Scheme
import aws.smithy.kotlin.runtime.net.url.Url
import java.io.File
import java.util.UUID

class S3Service(
    host: String,
    port: Int,
    region: String,
    accessKey: String,
    secretKey: String,
) {

    private val noteBucket = "note"

    private val s3Client = S3Client {
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


    suspend fun createBucket() {
        val bucketExists = s3Client
            .listBuckets()
            .buckets
            ?.any { it.name == noteBucket }
            ?: false

        if (!bucketExists) {
            s3Client.createBucket {
                bucket = noteBucket
            }
        }
    }

    // create

    suspend fun uploadFile(path: String, filename: String): String {
        val extension = filename
            .substringAfterLast(".", "")
            .let { if(it.isNotBlank()) ".$it" else "" }

        val filename = "${UUID.randomUUID()}$extension"

        s3Client.putObject {
            bucket = noteBucket
            key = filename
            body = File(path).asByteStream()
        }

        return filename
    }


    suspend fun downloadFile(filename: String): File {
        val request = GetObjectRequest {
            bucket = noteBucket
            key = filename
        }

        val file = File("uploads/$filename")

        s3Client.getObject(request) { response ->
            response.body?.writeToFile(file)
        }

        return file
    }

    suspend fun deleteFile(filename: String) {
        s3Client.deleteObject {
            bucket = noteBucket
            key = filename
        }
    }
}
