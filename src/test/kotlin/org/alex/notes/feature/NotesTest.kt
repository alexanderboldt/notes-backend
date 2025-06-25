package org.alex.notes.feature

import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.alex.notes.domain.Note
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.client.request.get
import io.ktor.client.request.put
import org.alex.notes.Fixtures
import org.alex.notes.repository.NoteDao

class NotesTest : StringSpec({

    // region create

    afterTest {
        NoteDao().deleteAll()
    }

    suspend fun postNote(note: Note): Int {
        return client.post(Routes.Note.MAIN) {
            setBody(note)
        }.body<Note>().id
    }

    infix fun Note.shouldBeNote(expected: Note) {
        id shouldBeGreaterThan 0
        title shouldBe expected.title
        description shouldBe expected.description
        createdAt shouldBeGreaterThan 0
        updatedAt shouldBeGreaterThan 0
    }
    
    "should create a note with valid request" {
        val response = client.post(Routes.Note.MAIN) {
            setBody(Fixtures.Notes.Domain.dinner)
        }
        val note = response.body<Note>()

        response shouldHaveStatus HttpStatusCode.Created
        note shouldBeNote Fixtures.Notes.Domain.dinner
    }

    // endregion

    // region read all

    "should return an empty list" {
        val response = client.get(Routes.Note.MAIN)
        val notes = response.body<List<Note>>()

        response shouldHaveStatus HttpStatusCode.OK
        notes shouldBe emptyList()
    }

    "should return a list with one note" {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.get(Routes.Note.MAIN)
        val notes = response.body<List<Note>>()

        response shouldHaveStatus HttpStatusCode.OK
        notes shouldHaveSize 1
    }

    "should return a list with ten notes" {
        (1..10).forEach { _ ->
            postNote(Fixtures.Notes.Domain.dinner)
        }

        val response = client.get(Routes.Note.MAIN)
        val notes = response.body<List<Note>>()

        response shouldHaveStatus HttpStatusCode.OK
        notes shouldHaveSize 10
    }

    // endregion

    // region read one

    "should throw bad-request with invalid id" {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.get(Routes.Note.DETAIL + 100)

        response shouldHaveStatus HttpStatusCode.BadRequest
    }

    "should return one note with valid id" {
        val id = postNote(Fixtures.Notes.Domain.dinner)

        val response = client.get(Routes.Note.DETAIL + id)
        val note = response.body<Note>()

        response shouldHaveStatus HttpStatusCode.OK
        note shouldBeNote Fixtures.Notes.Domain.dinner
    }

    // endregion

    // region update

    "should not update a note and throw a bad-request with invalid id" {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.put(Routes.Note.DETAIL + 100) {
            setBody(Fixtures.Notes.Domain.shopping)
        }

        response shouldHaveStatus HttpStatusCode.BadRequest
    }

    "should update a note with valid id" {
        val id = postNote(Fixtures.Notes.Domain.dinner)

        val response = client.put(Routes.Note.DETAIL + id) {
            setBody(Fixtures.Notes.Domain.shopping)
        }
        val note = response.body<Note>()

        response shouldHaveStatus HttpStatusCode.OK
        note shouldBeNote Fixtures.Notes.Domain.shopping
    }

    // endregion

    // region delete

    "should not delete a note and throw bad-request with invalid id" {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.delete(Routes.Note.DETAIL + 100)

        response shouldHaveStatus HttpStatusCode.BadRequest
    }

    "should delete a note with valid id" {
        val id = postNote(Fixtures.Notes.Domain.dinner)

        val response = client.delete(Routes.Note.DETAIL + id)

        response shouldHaveStatus HttpStatusCode.NoContent
    }

    // endregion
})
