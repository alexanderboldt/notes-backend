package feature

import com.alex.domain.Note
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.client.request.get
import io.ktor.client.request.put
import kotlinx.coroutines.runBlocking
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isNotEmpty
import kotlin.test.Test

class NotesTest : BaseTest() {

    // region create

    @Test
    fun `should create a note with valid request`(): Unit = runBlocking {
        val response = client.post(Routes.Note.MAIN) {
            setBody(Fixtures.Notes.Domain.dinner)
        }
        val note = response.body<Note>()

        expectThat(response.status).isEqualTo(HttpStatusCode.Created)
        note.isNote(Fixtures.Notes.Domain.dinner)
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`(): Unit = runBlocking {
        val response = client.get(Routes.Note.MAIN)
        val notes = response.body<List<Note>>()

        expectThat(response.status).isEqualTo(HttpStatusCode.OK)
        expectThat(notes).isEmpty()
    }

    @Test
    fun `should return a list with one note`(): Unit = runBlocking {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.get(Routes.Note.MAIN)
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

        val response = client.get(Routes.Note.MAIN)
        val notes = response.body<List<Note>>()

        expectThat(response.status).isEqualTo(HttpStatusCode.OK)
        expectThat(notes).isNotEmpty()
        expectThat(notes).hasSize(10)
    }

    // endregion

    // region read one

    @Test
    fun `should throw bad-request with invalid id`(): Unit = runBlocking {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.get(Routes.Note.DETAIL + 100)

        expectThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
    }

    @Test
    fun `should return one note with valid id`(): Unit = runBlocking {
        val id = postNote(Fixtures.Notes.Domain.dinner)

        val response = client.get(Routes.Note.DETAIL + id)
        val note = response.body<Note>()

        expectThat(response.status).isEqualTo(HttpStatusCode.OK)
        note.isNote(Fixtures.Notes.Domain.dinner)
    }

    // endregion

    // region update

    @Test
    fun `should not update a note and throw a bad-request with invalid id`(): Unit = runBlocking {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.put(Routes.Note.DETAIL + 100) {
            setBody(Fixtures.Notes.Domain.shopping)
        }

        expectThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
    }

    @Test
    fun `should update a note with valid id`(): Unit = runBlocking {
        val id = postNote(Fixtures.Notes.Domain.dinner)

        val response = client.put(Routes.Note.DETAIL + id) {
            setBody(Fixtures.Notes.Domain.shopping)
        }
        val note = response.body<Note>()

        expectThat(response.status).isEqualTo(HttpStatusCode.OK)
        note.isNote(Fixtures.Notes.Domain.shopping)
    }

    // endregion

    // region delete

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
            setBody(note)
        }.body<Note>().id
    }

    private fun Note.isNote(expected: Note) {
        expectThat(id).isGreaterThan(0)
        expectThat(title).isEqualTo(expected.title)
        expectThat(description).isEqualTo(expected.description)
        expectThat(createdAt).isGreaterThan(0)
        expectThat(updatedAt).isGreaterThan(0)
    }
}