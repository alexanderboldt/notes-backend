package org.alex.notes.service

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import org.alex.notes.domain.NoteResponse
import org.alex.notes.repository.NoteDao
import org.alex.notes.repository.toDomain
import org.alex.notes.utils.BadRequestThrowable
import java.io.File

class NoteImageService(private val s3Service: S3Service, private val noteDao: NoteDao) {

    suspend fun uploadImage(id: Int, multipart: MultiPartData): NoteResponse {

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

        val filename = s3Service.uploadFile(file.absolutePath, file.name)

        return noteDao
            .updateFilename(id, filename)
            ?.toDomain()
            ?: throw BadRequestThrowable()
    }

    suspend fun downloadImage(id: Int): Pair<File, String> {
        val filename = noteDao.get(id)?.filename ?: throw BadRequestThrowable()

        return s3Service.downloadFile(filename) to filename
    }

    suspend fun deleteImage(id: Int) {
        val filename = noteDao.get(id)?.filename ?: throw BadRequestThrowable()

        s3Service.deleteFile(filename)

        noteDao.deleteFilename(id)
    }
}
