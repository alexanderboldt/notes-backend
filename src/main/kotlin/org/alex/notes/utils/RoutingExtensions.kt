package org.alex.notes.utils

import io.ktor.server.routing.RoutingContext
import io.ktor.server.util.getOrFail
import org.jetbrains.exposed.v1.core.Column

val RoutingContext.id: Int
    get() = call.pathParameters.getOrFail<Int>(PathParam.ID)

fun RoutingContext.getSort(columns: List<Column<*>>) = call.queryParameters[QueryParam.SORT]?.convertToSort(columns)
