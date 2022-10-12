package com.alex.main.kotlin.feature

import com.alex.main.kotlin.repository.database.DbModelNote
import com.alex.main.kotlin.repository.database.NoteDao
import io.ktor.server.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*
import com.alex.main.kotlin.repository.rest.RestModelError
import com.alex.main.kotlin.repository.rest.RestModelNotePost
import com.alex.main.kotlin.repository.rest.RestModelNotePut
import com.alex.main.kotlin.repository.toRestModelGet
import com.alex.main.kotlin.utils.*
import io.ktor.server.response.*
import java.util.*

fun Route.notesRouting() {

    val noteDao = NoteDao()

    val pathV1 = "v1"
    val pathV2 = "v2"

    val resource = "notes"

    route(pathV1) {
        route(resource) {

            // create

            post {
                val note = call.safeReceiveOrNull<RestModelNotePost>() ?: return@post call.respond(HttpStatusCode.BadRequest, RestModelError("Unexpected body-request"))

                val date = Date().time
                noteDao.save(DbModelNote(0, note.title, note.description, date, date))
                call.respond(HttpStatusCode.Created, noteDao.getAll().toRestModelGet())
            }

            // read

            get {
                call.response.header("Deprecated", "true")
                call.respond(HttpStatusCode.OK, noteDao.getAll().toRestModelGet())
            }

            get(PARAMETER_ID) {
                val id = call.idParameter ?: return@get call.respond(HttpStatusCode.BadRequest, RestModelError("Invalid id!"))

                val note = noteDao.get(id)
                when (note != null) {
                    true -> call.respond(HttpStatusCode.OK, note.toRestModelGet())
                    false -> call.respond(HttpStatusCode.BadRequest, RestModelError("Note not found with given id!"))
                }
            }

            // update

            put(PARAMETER_ID) {
                val note = call.safeReceiveOrNull<RestModelNotePut>() ?: return@put call.respond(HttpStatusCode.BadRequest, RestModelError("Unexpected body-request!"))
                val id = call.idParameter ?: return@put call.respond(HttpStatusCode.BadRequest, RestModelError("Invalid id!"))

                noteDao
                    .get(id)
                    ?.apply {
                        title = note.title ?: title
                        description = note.description ?: description
                        updatedAt = Date().time
                    }?.apply {
                        noteDao.update(this)
                        call.respond(HttpStatusCode.OK, noteDao.get(id)!!.toRestModelGet())
                    } ?: call.respond(HttpStatusCode.BadRequest, RestModelError("Could not update note!"))
            }

            // delete

            delete {
                when (noteDao.delete()) {
                    true -> call.respond(HttpStatusCode.OK, noteDao.getAll().toRestModelGet())
                    false -> call.respond(HttpStatusCode.Conflict, RestModelError("Could not delete all notes!"))
                }
            }

            delete(PARAMETER_ID) {
                val id = call.idParameter ?: return@delete call.respond(HttpStatusCode.BadRequest, RestModelError("Invalid id!"))

                when (noteDao.delete(id)) {
                    true -> call.respond(HttpStatusCode.OK, noteDao.getAll().toRestModelGet())
                    false -> call.respond(HttpStatusCode.Conflict, RestModelError("Could not delete note!"))
                }
            }
        }
    }

    route(pathV2) {
        route(resource) {
            get {
                call.respond(
                    HttpStatusCode.OK,
                    noteDao.getAll(
                        call.sortParameter,
                        call.offsetParameter,
                        call.limitParameter
                    ).toRestModelGet()
                )
            }
        }
    }
}