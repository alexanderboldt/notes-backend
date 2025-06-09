package com.alex.utils

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder
import kotlin.collections.mapNotNull
import kotlin.text.split

/**
 * Converts a sort parameter from [String] to `List<Pair<Column<*>, SortOrder>>`.
 *
 * @param columns The columns of the required table to compare as a [List] of [Column]-objects.
 * @return The prepared sort-objects as `List<Pair<Column<*>, SortOrder>>`.
 */
fun String.convertToSort(columns: List<Column<*>>): List<Pair<Column<*>, SortOrder>> {
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