package feature

import com.alex.domain.Note
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEmpty
import strikt.assertions.isNotEqualTo
import strikt.assertions.isNotNull
import kotlin.test.Test

class NotesTest : BaseTest() {

    // region create

    @Test
    fun `should create a note with valid request`(): Unit = runBlocking {
        val response = client.post(Routes.Note.MAIN) {
            contentType(ContentType.Application.Json)
            setBody(Fixtures.Notes.Domain.dinner)
        }

        val note = response.body<Note>()

        expectThat(response.status).isEqualTo(HttpStatusCode.Created)
        expectThat(note.id).isNotNull()
        expectThat(note.title).isEqualTo(Fixtures.Notes.Domain.dinner.title)
        expectThat(note.description).isEqualTo(Fixtures.Notes.Domain.dinner.description)
        expectThat(note.createdAt).isNotEqualTo(0)
        expectThat(note.updatedAt).isNotEqualTo(0)
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`(): Unit = runBlocking {
        val response = client.get(Routes.Note.MAIN) {
            contentType(ContentType.Application.Json)
        }

        val notes = response.body<List<Note>>()

        expectThat(response.status).isEqualTo(HttpStatusCode.OK)
        expectThat(notes).isEmpty()
    }

    @Test
    fun `should return a list with one note`(): Unit = runBlocking {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.get(Routes.Note.MAIN) {
            contentType(ContentType.Application.Json)
        }

        val notes = response.body<List<Note>>()

        expectThat(response.status).isEqualTo(HttpStatusCode.OK)
        expectThat(notes).isNotEmpty()
        expectThat(notes).hasSize(1)
    }

    @Test
    fun `should return a list with ten notes`(): Unit = runBlocking {
        (1..10).forEach { _ ->
            postNote(Fixtures.Notes.Domain.dinner)
        }

        val response = client.get(Routes.Note.MAIN) {
            contentType(ContentType.Application.Json)
        }

        val notes = response.body<List<Note>>()

        expectThat(response.status).isEqualTo(HttpStatusCode.OK)
        expectThat(notes).isNotEmpty()
        expectThat(notes).hasSize(10)
    }

    // endregion

    // region read delete

    @Test
    fun `should not delete a note and throw bad-request with invalid id`(): Unit = runBlocking {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.delete(Routes.Note.DETAIL + 100)

        expectThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
    }

    @Test
    fun `should delete a note with valid id`(): Unit = runBlocking {
        val id = postNote(Fixtures.Notes.Domain.dinner)

        val response = client.delete(Routes.Note.DETAIL + id)

        expectThat(response.status).isEqualTo(HttpStatusCode.NoContent)
    }

    // endregion

    private suspend fun postNote(note: Note): Int {
        return client.post(Routes.Note.MAIN) {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body<Note>().id!!
    }
}