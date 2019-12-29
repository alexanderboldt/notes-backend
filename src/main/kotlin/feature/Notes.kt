package com.alex.main.kotlin.feature

import com.alex.main.kotlin.repository.Note
import com.alex.main.kotlin.repository.NotesList
import com.alex.main.kotlin.repository.NotesRepository
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.*

object Notes {

    private val notesRepository by lazy { NotesRepository() }

    // -----------------------------------------------------------------------------

    fun routing(): Routing.() -> Unit = {
        get("notes") {
            call.respond(HttpStatusCode.OK, NotesList(notesRepository.get()))
        }

        get("notes/{id}") {
            call
                .parameters["id"]
                ?.toIntOrNull()
                ?.also { id ->
                    notesRepository.get(id)?.apply {
                        call.respond(HttpStatusCode.OK, this)
                    } ?: run {
                        call.respond(HttpStatusCode.BadRequest, com.alex.main.kotlin.repository.Error("Note not found with given id!"))
                    }
                }
                ?: run {
                    call.respond(HttpStatusCode.BadRequest, com.alex.main.kotlin.repository.Error("Invalid id!"))
                }
        }

        post("notes") {
            call
                .receiveOrNull<Note>()
                ?.apply {
                    notesRepository.save(this)
                    call.respond(HttpStatusCode.Created, NotesList(notesRepository.get()))
                }
                ?: run {
                    call.respond(HttpStatusCode.BadRequest, com.alex.main.kotlin.repository.Error("Unexpected body-request"))
                }
        }

        put("notes/{id}") {
            call
                .receiveOrNull<Note>()
                ?.also { note ->
                    call
                        .parameters["id"]
                        ?.toIntOrNull()
                        ?.also { id ->
                            notesRepository.update(note.copy(id = id)).also { isUpdated ->
                                if (isUpdated) {
                                    call.respond(HttpStatusCode.OK, notesRepository.get(id)!!)
                                } else {
                                    call.respond(HttpStatusCode.BadRequest, com.alex.main.kotlin.repository.Error("Could not update note!"))
                                }
                            }
                        }
                        ?: run {
                            call.respond(HttpStatusCode.BadRequest, com.alex.main.kotlin.repository.Error("Invalid id!"))
                        }
                }
                ?: run {
                    call.respond(HttpStatusCode.BadRequest, com.alex.main.kotlin.repository.Error("Unexpected body-request!"))
                }
        }

        delete("notes") {
            when (notesRepository.delete()) {
                true -> call.respond(HttpStatusCode.OK, NotesList(notesRepository.get()))
                false -> call.respond(HttpStatusCode.Conflict, com.alex.main.kotlin.repository.Error("Could not delete all notes!"))
            }
        }

        delete("notes/{id}") {
            call.parameters["id"]?.toIntOrNull()?.apply {
                when (notesRepository.delete(this)) {
                    true -> call.respond(HttpStatusCode.OK, NotesList(notesRepository.get()))
                    false -> call.respond(HttpStatusCode.Conflict, com.alex.main.kotlin.repository.Error("Could not delete note!"))
                }
            } ?: run {
                call.respond(HttpStatusCode.Conflict, com.alex.main.kotlin.repository.Error("Invalid id!"))
            }
        }
    }
}