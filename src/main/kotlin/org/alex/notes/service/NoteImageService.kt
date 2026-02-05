package org.alex.notes.service

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import org.alex.notes.utils.BadRequestThrowable
import java.io.File
import java.io.InputStream

class NoteImageService(private val s3Service: S3Service) {

    suspend fun uploadImage(multipart: MultiPartData): String {

        var file: File? = null

        multipart.forEachPart { part ->
            if (part is PartData.FileItem && part.name == "image") {
                val filename = part.originalFileName as String
                file = File("uploads/$filename")

                file.parentFile.mkdirs()
                part.provider().copyAndClose(file.writeChannel())
            }
            part.dispose()
        }

        if (file == null) throw BadRequestThrowable()

        s3Service.uploadFile(file.absolutePath, file.name)

        return "done"
    }

    fun downloadImage(): Pair<InputStream, String> {
        return s3Service.downloadFile("90bb4257-628d-4ef1-8379-085563ac6192.jpg") to "90bb4257-628d-4ef1-8379-085563ac6192.jpg"
    }

    suspend fun deleteImage() {
        s3Service.deleteFile("90bb4257-628d-4ef1-8379-085563ac6192.jpg")
    }
}
