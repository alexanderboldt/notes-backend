package com.alex.utils

import io.ktor.server.application.*
import io.ktor.util.reflect.*

suspend inline fun <reified T> ApplicationCall.safeReceiveOrNull(): T? = try {
    receiveNullable(typeInfo<T>())
} catch (throwable: Exception) {
    null
}