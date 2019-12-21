package com.alex.main.kotlin.feature

import com.alex.main.kotlin.repository.NotesRepository
import com.alex.main.kotlin.utils.respondError
import com.alex.main.kotlin.utils.respondJson
import com.alex.main.kotlin.utils.toJson
import com.alex.main.kotlin.utils.toNote
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