package com.alex.main.kotlin.utils

import com.alex.main.kotlin.repository.Note
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext

// models <-> json

fun String.toNote(): Note? {
    return try {
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(Note::class.java)
            .fromJson(this)
    } catch (exception: Exception) {
        null
    }
}

fun Note.toJson(): String {
    return Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(Note::class.java)
        .toJson(this)
}

fun List<Note>.toJson(): String {
    return Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter<List<Note>>(Types.newParameterizedType(List::class.java, Note::class.java))
            .toJson(this)
}

// ApplicationCall

suspend fun ApplicationCall.respondJson(json: String, statusCode: HttpStatusCode) {
    respondText(json, ContentType.Application.Json, statusCode)
}

suspend fun ApplicationCall.respondError(message: String, statusCode: HttpStatusCode) {
    Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(com.alex.main.kotlin.repository.Error::class.java)
        .apply {
            respondText(toJson(com.alex.main.kotlin.repository.Error(message)), ContentType.Application.Json, statusCode)
        }
}

fun <T> Route.postNotNull(path: String, clazz: Class<T>, block: (PipelineContext<Unit, ApplicationCall>, T) -> Unit) {
    post(path) {
        try {
            val model = Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter(clazz)
                .fromJson(call.receiveText())!!

            block(this, model)
        } catch (exception: Exception) {
            call.respondError("Unexpected body-request", HttpStatusCode.BadRequest)
        }
    }
}