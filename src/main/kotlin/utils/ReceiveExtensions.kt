package com.alex.main.kotlin.utils

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.reflect.*

suspend inline fun <reified T> ApplicationCall.safeReceiveOrNull(): T? = try {
    receiveNullable(typeInfo<T>())
} catch (throwable: Exception) {
    null
}