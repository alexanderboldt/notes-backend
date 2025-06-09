package com.alex.utils

import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.util.reflect.typeInfo

val ApplicationCall.sortParameter: String?
    get() = parameters["sort"]

suspend inline fun <reified T> ApplicationCall.receiveOrThrow(): T = try {
    receive(typeInfo<T>())
} catch (_: Exception) {
    throw BadRequestException(ErrorMessages.BODY_REQUEST)
}