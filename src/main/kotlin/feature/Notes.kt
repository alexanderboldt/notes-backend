package com.alex.main.kotlin.feature

import com.alex.main.kotlin.repository.Note
import com.alex.main.kotlin.repository.NotesRepository
import com.alex.main.kotlin.utils.*
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.routing.*

object Notes {

    private val notesRepository by lazy { NotesRepository() }

    // -----------------------------------------------------------------------------

    fun routing(): Routing.() -> Unit = {
        get("notes") {
            call.respondJson(notesRepository.get().toJson(), HttpStatusCode.OK)
        }

        get("notes/{id}") {
            call
                .parameters["id"]
                ?.toIntOrNull()
                ?.also { id ->
                    notesRepository.get(id)?.apply {
                        call.respondJson(this.toJson(), HttpStatusCode.OK)
                    } ?: run {
                        call.respondError("Note not found with given id!", HttpStatusCode.BadRequest)
                    }
                }
                ?: run {
                    call.respondError("Invalid id!", HttpStatusCode.BadRequest)
                }
        }

        post("notes") {
            call
                    .receiveText()
                    .toNote()
                    ?.apply {
                        notesRepository.save(this)
                        call.respondJson(notesRepository.get().toJson(), HttpStatusCode.Created)
                    }
                    ?: run {
                        call.respondError("Unexpected body-request", HttpStatusCode.BadRequest)
                    }
        }

        put("notes/{id}") {
            call
                .receiveText()
                .toNote()
                ?.also { note ->
                    call
                        .parameters["id"]
                        ?.toIntOrNull()
                        ?.also { id ->
                            notesRepository.update(note.copy(id = id)).also { isUpdated ->
                                if (isUpdated) {
                                    call.respondJson(notesRepository.get(id)!!.toJson(), HttpStatusCode.OK)
                                } else {
                                    call.respondError("Could not update note", HttpStatusCode.Conflict)
                                }
                            }
                        }
                        ?: run {
                            call.respondError("Invalid id", HttpStatusCode.BadRequest)
                        }
                }
                ?: run {
                    call.respondError("Unexpected body-request", HttpStatusCode.BadRequest)
                }
        }

        delete("notes") {
            when (notesRepository.delete()) {
                true -> call.respondJson(notesRepository.get().toJson(), HttpStatusCode.OK)
                false -> call.respondError("Could not delete all notes", HttpStatusCode.Conflict)
            }
        }

        delete("notes/{id}") {
            call.parameters["id"]?.toIntOrNull()?.apply {
                when (notesRepository.delete(this)) {
                    true -> call.respondJson(notesRepository.get().toJson(), HttpStatusCode.OK)
                    false -> call.respondError("Could not delete note", HttpStatusCode.Conflict)
                }
            } ?: run {
                call.respondError("Invalid ID", HttpStatusCode.BadRequest)
            }
        }
    }
}