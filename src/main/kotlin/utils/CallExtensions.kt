package com.alex.utils

import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.util.reflect.typeInfo

private const val PARAMETER_SORT = "sort"
private const val PARAMETER_OFFSET = "offset"
private const val PARAMETER_LIMIT = "limit"

val ApplicationCall.idOrThrow: Int
    get() {
        return parameters["id"]
            ?.toIntOrNull()
            ?: throw BadRequestException(ErrorMessages.INVALID_ID)
    }

val ApplicationCall.sortParameter: Pair<String, Boolean>?
    get() {
        return parameters[PARAMETER_SORT]?.run {
            when (startsWith("-")) {
                true -> substring(1, length) to false
                false -> this to true
            }
        }
}
val ApplicationCall.offsetParameter: Long?
    get() = parameters[PARAMETER_OFFSET]?.toLongOrNull()?.let { if (it < 0) 0 else it }

val ApplicationCall.limitParameter: Int?
    get() = parameters[PARAMETER_LIMIT]?.toIntOrNull()?.let { if (it < 0) 0 else it }

suspend inline fun <reified T> ApplicationCall.receiveOrThrow(): T = try {
    receive(typeInfo<T>())
} catch (throwable: Exception) {
    throw BadRequestException(ErrorMessages.BODY_REQUEST)
}