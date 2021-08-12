package com.alex.main.kotlin.feature

import com.alex.main.kotlin.repository.Note
import com.alex.main.kotlin.repository.NotesList
import com.alex.main.kotlin.repository.NotesRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.routing.*
import com.alex.main.kotlin.repository.Error
import com.alex.main.kotlin.utils.getLimitParameter
import com.alex.main.kotlin.utils.getOffsetParameter
import com.alex.main.kotlin.utils.getSortParameter
import io.ktor.request.*
import io.ktor.response.*
import java.util.*

object Notes {

    private val notesRepository by lazy { NotesRepository() }

    // -----------------------------------------------------------------------------

    fun routing(): Routing.() -> Unit = {

        // create

        post("v1/notes") {
            call
                .receiveOrNull<Note>()
                ?.apply {
                    notesRepository.save(this)
                    call.respond(HttpStatusCode.Created, NotesList(notesRepository.getAll()))
                } ?: run {
                    call.respond(HttpStatusCode.BadRequest, Error("Unexpected body-request"))
                }
        }

        // read

        get("v1/notes") {
            call.response.header("Deprecated", "true")
            call.respond(HttpStatusCode.OK, NotesList(notesRepository.getAll()))
        }

        get("v2/notes") {
            call.respond(HttpStatusCode.OK, NotesList(notesRepository.getAll(
                call.getSortParameter(),
                call.getOffsetParameter(),
                call.getLimitParameter())))
        }

        get("v1/notes/{id}") {
            call
                .parameters["id"]
                ?.toIntOrNull()
                ?.also { id ->
                    notesRepository.get(id)?.apply {
                        call.respond(HttpStatusCode.OK, this)
                    } ?: run {
                        call.respond(HttpStatusCode.BadRequest, Error("Note not found with given id!"))
                    }
                } ?: run {
                    call.respond(HttpStatusCode.BadRequest, Error("Invalid id!"))
                }
        }

        // update

        put("v1/notes/{id}") {
            call
                .receiveOrNull<Note>()
                ?.also { note ->
                    call
                        .parameters["id"]
                        ?.toIntOrNull()
                        ?.also { id ->
                            notesRepository
                                .get(id)
                                ?.apply {
                                    title = note.title
                                    description = note.description
                                    updatedAt = Date().time
                                }?.apply {
                                    notesRepository.update(this)
                                    call.respond(HttpStatusCode.OK, notesRepository.get(id)!!)
                                }?: call.respond(HttpStatusCode.BadRequest, Error("Could not update note!"))
                        } ?: run {
                            call.respond(HttpStatusCode.BadRequest, Error("Invalid id!"))
                        }
                } ?: run {
                    call.respond(HttpStatusCode.BadRequest, Error("Unexpected body-request!"))
                }
        }

        // delete

        // it's possible to delete a whole collection, but not desirable
        delete("v1/notes") {
            when (notesRepository.delete()) {
                true -> call.respond(HttpStatusCode.OK, NotesList(notesRepository.getAll()))
                false -> call.respond(HttpStatusCode.Conflict, Error("Could not delete all notes!"))
            }
        }

        delete("v1/notes/{id}") {
            call.parameters["id"]?.toIntOrNull()?.apply {
                when (notesRepository.delete(this)) {
                    true -> call.respond(HttpStatusCode.OK, NotesList(notesRepository.getAll()))
                    false -> call.respond(HttpStatusCode.Conflict, Error("Could not delete note!"))
                }
            } ?: run {
                call.respond(HttpStatusCode.Conflict, Error("Invalid id!"))
            }
        }
    }
}