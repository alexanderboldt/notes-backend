package org.alex.notes.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.createBucket
import aws.sdk.kotlin.services.s3.deleteObject
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.putObject
import aws.smithy.kotlin.runtime.content.asByteStream
import aws.smithy.kotlin.runtime.content.writeToFile
import java.io.File
import java.util.UUID

class S3Service(private val s3Client: S3Client) {

    private val noteBucket = "note"


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
