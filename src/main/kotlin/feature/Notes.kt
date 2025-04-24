package com.alex.feature

import com.alex.repository.database.NoteDao
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*
import com.alex.repository.rest.RestModelNotePost
import com.alex.repository.rest.RestModelNotePut
import com.alex.repository.toDbModel
import com.alex.repository.toRestModelGet
import com.alex.utils.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.notesRouting() {

    route("api/v1/notes") {

        val noteDao: NoteDao by inject()

        // create

        post {
            val note = call.receiveOrThrow<RestModelNotePost>().toDbModel()
            call.respond(HttpStatusCode.Created, noteDao.save(note).toRestModelGet())
        }

        // read

        get {
            call.respond(noteDao.getAll(call.offsetParameter, call.limitParameter).toRestModelGet())
        }

        get("/{id}") {
            val note = noteDao
                .get(call.idOrThrow)
                ?.toRestModelGet()
                ?: throw BadRequestException(ErrorMessages.INVALID_ID)

            call.respond(note)
        }

        // update

        put("/{id}") {
            val note = call.receiveOrThrow<RestModelNotePut>()
            val id = call.idOrThrow

            noteDao
                .get(id)
                ?.apply {
                    title = note.title ?: title
                    description = note.description ?: description
                    updatedAt = Date().time
                }?.apply {
                    noteDao.update(this)
                    call.respond(noteDao.get(id)!!.toRestModelGet())
                } ?: throw BadRequestException(ErrorMessages.UPDATE_FAILED)
        }

        // delete

        delete("/{id}") {
            if (!noteDao.delete(call.idOrThrow)) throw BadRequestException(ErrorMessages.INVALID_ID)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}