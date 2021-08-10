package com.alex.main.kotlin.utils

import com.squareup.moshi.Moshi
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.content.TextContent
import io.ktor.features.ContentConverter
import io.ktor.features.suitableCharset
import io.ktor.http.ContentType
import io.ktor.http.withCharset
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.util.pipeline.PipelineContext
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlin.reflect.jvm.jvmErasure

class MoshiConverter(private val moshi: Moshi = Moshi.Builder().build()) : ContentConverter {
    override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
        val request = context.subject
        val channel = request.value as? ByteReadChannel ?: return null
        val reader = channel.toInputStream().bufferedReader().readText()

        return try {
            moshi.adapter(request.typeInfo.jvmErasure.javaObjectType).fromJson(reader)
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun convertForSend(
        context: PipelineContext<Any, ApplicationCall>,
        contentType: ContentType,
        value: Any
    ): Any? {
        return TextContent(moshi.adapter(value.javaClass).toJson(value), contentType.withCharset(context.call.suitableCharset()))
    }
}