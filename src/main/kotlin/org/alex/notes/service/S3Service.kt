package org.alex.notes.service

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI
import java.nio.file.Path
import java.util.UUID

class S3Service(
    url: String,
    region: String,
    accessKey: String,
    secretKey: String,
) {

    private val s3Client = S3Client.builder()
        .endpointOverride(URI.create(url))
        .region(Region.of(region))
        .forcePathStyle(true)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        ).build()

    private val bucket = "note"

    init {
        // create the bucket if it not exists yet
        val bucketExists = s3Client
            .listBuckets { it.build() }
            .buckets()
            .any { it.name() == bucket }

        if (!bucketExists) {
            s3Client.createBucket { it.bucket(bucket).build() }
        }
    }

    // create

    fun uploadFile(path: String, filename: String): String {
        val extension = filename
            .substringAfterLast(".", "")
            .let { if(it.isNotBlank()) ".$it" else "" }

        val filename = "${UUID.randomUUID()}$extension"

        s3Client.putObject(
            { it.bucket(bucket).key(filename).build() },
            RequestBody.fromFile(Path.of(path))
        )

        return filename
    }
}
