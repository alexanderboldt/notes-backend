package org.alex.notes.utils

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.SortOrder
import kotlin.collections.mapNotNull
import kotlin.text.split

typealias Sort = List<Pair<Column<*>, SortOrder>>

/**
 * Converts a sort parameter from [String] to [Sort].
 *
 * @param columns The columns of the required table to compare as a [List] of [Column]-objects.
 * @return The prepared sort-objects as [Sort].
 */
fun String.convertToSort(columns: List<Column<*>>): Sort {
    return split(",")
        .mapNotNull { sort ->
            val (expression, order) = when (sort.startsWith("-")) {
                true -> sort.drop(1) to SortOrder.DESC
                else -> sort to SortOrder.ASC
            }

            columns
                .find { it.name == expression }
                ?.let { it to order }
        }
}
