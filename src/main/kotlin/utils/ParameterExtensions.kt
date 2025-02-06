package com.alex.utils

import io.ktor.server.application.*

const val PARAMETER_ID = "{id}"
private const val PARAMETER_SORT = "sort"
private const val PARAMETER_OFFSET = "offset"
private const val PARAMETER_LIMIT = "limit"

val ApplicationCall.idParameter: Int?
    get() = parameters["id"]?.toIntOrNull()

val ApplicationCall.sortParameter: Pair<String, Boolean>?
    get() {
        return parameters[PARAMETER_SORT]?.run {
            when (startsWith("-")) {
                true -> substring(1, length) to false
                false -> this to true
            }
        }
}
val ApplicationCall.offsetParameter: Int?
    get() = parameters[PARAMETER_OFFSET]?.toIntOrNull()

val ApplicationCall.limitParameter: Int?
    get() = parameters[PARAMETER_LIMIT]?.toIntOrNull()?.let { if (it < 0) 0 else it }