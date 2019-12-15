package com.alex.main.kotlin.notes

import com.alex.main.kotlin.repository.Note
import com.alex.main.kotlin.repository.NotesRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

object Notes {

    private val notesRepository by lazy { NotesRepository() }

    val moshiListNotes = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<List<Note>>(Types.newParameterizedType(List::class.java, Note::class.java))
    val moshiNote = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Note::class.java)
    val moshiError = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Error::class.java)

    // -----------------------------------------------------------------------------

    fun routing(): Routing.() -> Unit = {

        get("notes") {
            call.respondText(moshiListNotes.toJson(notesRepository.get()), ContentType.Application.Json, HttpStatusCode.OK)
        }
        post("notes") {
            try {
                val note = moshiNote.fromJson(call.receiveText())
                if (note != null) {
                    notesRepository.save(note)
                    call.respondText(moshiListNotes.toJson(notesRepository.get()), ContentType.Application.Json, HttpStatusCode.Created)
                } else {
                    call.respondText(moshiError.toJson(Error("Unexpected body-request")), ContentType.Application.Json, HttpStatusCode.BadRequest)
                }
            } catch (exception: Exception) {
                call.respondText(moshiError.toJson(Error("Unexpected body-request")), ContentType.Application.Json, HttpStatusCode.BadRequest)
            }
        }
    }
}