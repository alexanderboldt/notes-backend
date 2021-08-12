package com.alex.main.kotlin.utils

import io.ktor.application.*

private const val PARAMETER_SORT = "sort"
private const val PARAMETER_OFFSET = "offset"
private const val PARAMETER_LIMIT = "limit"

fun ApplicationCall.getSortParameter(): Pair<String,Boolean>? {
    return parameters[PARAMETER_SORT]?.run {
        if (startsWith("-")) {
            substring(1, length) to false
        } else {
            this to true
        }
    }
}

fun ApplicationCall.getOffsetParameter(): Int? = parameters[PARAMETER_OFFSET]?.toIntOrNull()
fun ApplicationCall.getLimitParameter(): Int? = parameters[PARAMETER_LIMIT]?.toIntOrNull()?.let { if (it < 0) 0 else it }