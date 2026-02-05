package org.alex.notes.service

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import org.alex.notes.utils.BadRequestThrowable
import java.io.File

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
}
