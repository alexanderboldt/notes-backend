package org.alex.notes.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.alex.notes.repository.NoteTable
import org.jetbrains.exposed.sql.SortOrder
import kotlin.collections.emptyList

class SortExtensionsTest : StringSpec ({

    "convert empty string to empty list" {
        val sort = "".convertToSort(NoteTable.columns)

        sort shouldBe emptyList()
    }
    
    "should convert string with just a comma to empty list" {
        val sort = ",".convertToSort(NoteTable.columns)

        sort shouldBe emptyList()
    }
    
    "should convert sort parameter with unknown value empty list" {
        val sort = "number".convertToSort(NoteTable.columns)

        sort shouldBe emptyList()
    }
    
    "should convert sort parameter with id asc to valid list" {
        val sort = "id".convertToSort(NoteTable.columns)

        sort shouldHaveSize 1
        sort.forExactly(1) {
            it.first shouldBe NoteTable.id
            it.second shouldBe SortOrder.ASC
        }
    }
    
    "should convert sort parameter with id desc to valid list" {
        val sort = "-id".convertToSort(NoteTable.columns)

        sort shouldHaveSize 1
        sort.forExactly(1) {
            it.first shouldBe NoteTable.id
            it.second shouldBe SortOrder.DESC
        }
    }
    
    "should convert sort parameter with id desc and title asc to valid list" {
        val sort = "-id,title".convertToSort(NoteTable.columns)

        sort shouldHaveSize 2
        sort[0].apply {
            first shouldBe NoteTable.id
            second shouldBe SortOrder.DESC
        }
        sort[1].apply {
            first shouldBe NoteTable.title
            second shouldBe SortOrder.ASC
        }
    }
    
    "should convert sort parameter with containing unknown value to valid list" {
        val sort = "-id,number,-title".convertToSort(NoteTable.columns)

        sort shouldHaveSize 2
        sort[0].apply {
            first shouldBe NoteTable.id
            second shouldBe SortOrder.DESC
        }
        sort[1].apply {
            first shouldBe NoteTable.title
            second shouldBe SortOrder.DESC
        }
    }
})
