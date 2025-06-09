package com.alex.utils

import com.alex.repository.NoteTable
import org.jetbrains.exposed.sql.SortOrder
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEmpty
import kotlin.test.Test

class SortExtensionsTest {

    @Test
    fun `should convert empty string to empty list`() {
        val sort = "".convertToSort(NoteTable.columns)

        expectThat(sort).isEmpty()
    }

    @Test
    fun `should convert string with just a comma to empty list`() {
        val sort = ",".convertToSort(NoteTable.columns)

        expectThat(sort).isEmpty()
    }

    @Test
    fun `should convert sort parameter with unknown value empty list`() {
        val sort = "number".convertToSort(NoteTable.columns)

        expectThat(sort).isEmpty()
    }

    @Test
    fun `should convert sort parameter with id asc to valid list`() {
        val sort = "id".convertToSort(NoteTable.columns)

        expectThat(sort).isNotEmpty()
        expectThat(sort).hasSize(1)
        expectThat(sort[0].first).isEqualTo(NoteTable.id)
        expectThat(sort[0].second).isEqualTo(SortOrder.ASC)
    }

    @Test
    fun `should convert sort parameter with id desc to valid list`() {
        val sort = "-id".convertToSort(NoteTable.columns)

        expectThat(sort).isNotEmpty()
        expectThat(sort).hasSize(1)
        expectThat(sort[0].first).isEqualTo(NoteTable.id)
        expectThat(sort[0].second).isEqualTo(SortOrder.DESC)
    }

    @Test
    fun `should convert sort parameter with id desc and title asc to valid list`() {
        val sort = "-id,title".convertToSort(NoteTable.columns)

        expectThat(sort).isNotEmpty()
        expectThat(sort).hasSize(2)
        expectThat(sort[0].first).isEqualTo(NoteTable.id)
        expectThat(sort[0].second).isEqualTo(SortOrder.DESC)
        expectThat(sort[1].first).isEqualTo(NoteTable.title)
        expectThat(sort[1].second).isEqualTo(SortOrder.ASC)
    }

    @Test
    fun `should convert sort parameter with containing unknown value to valid list`() {
        val sort = "-id,number,-title".convertToSort(NoteTable.columns)

        expectThat(sort).isNotEmpty()
        expectThat(sort).hasSize(2)
        expectThat(sort[0].first).isEqualTo(NoteTable.id)
        expectThat(sort[0].second).isEqualTo(SortOrder.DESC)
        expectThat(sort[1].first).isEqualTo(NoteTable.title)
        expectThat(sort[1].second).isEqualTo(SortOrder.DESC)
    }
}