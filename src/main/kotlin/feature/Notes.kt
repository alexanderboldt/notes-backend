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
import java.util.*

fun Route.notesRouting(noteDao: NoteDao) {

    route("api/v1/notes") {

        // create

        post {
            val note = call.safeReceiveOrNull<RestModelNotePost>()?.toDbModel() ?: throw BadRequestException("Unexpected body-request")

            call.respond(HttpStatusCode.Created, noteDao.save(note).toRestModelGet())
        }

        // read

        get {
            call.respond(noteDao.getAll(call.offsetParameter, call.limitParameter).toRestModelGet())
        }

        get("/{id}") {
            val id = call.idParameter ?: throw BadRequestException("Invalid id!")

            val note = noteDao
                .get(id)
                ?.toRestModelGet()
                ?: throw BadRequestException("Invalid id!")

            call.respond(note)
        }

        // update

        put("/{id}") {
            val note = call.safeReceiveOrNull<RestModelNotePut>() ?: throw BadRequestException("Unexpected body-request!")
            val id = call.idParameter ?: throw BadRequestException("Invalid id!")

            noteDao
                .get(id)
                ?.apply {
                    title = note.title ?: title
                    description = note.description ?: description
                    updatedAt = Date().time
                }?.apply {
                    noteDao.update(this)
                    call.respond(noteDao.get(id)!!.toRestModelGet())
                } ?: throw BadRequestException("Could not update note!")
        }

        // delete

        delete("/{id}") {
            val id = call.idParameter ?: throw BadRequestException("Invalid id!")

            noteDao.get(id) ?: throw BadRequestException("Could not delete note!")
            noteDao.delete(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}