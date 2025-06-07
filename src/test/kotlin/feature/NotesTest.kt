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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NotesTest : BaseTest() {

    // region create

    @Test
    fun `should create a note with valid request`(): Unit = runBlocking {
        val response = client.post(Routes.Note.MAIN) {
            contentType(ContentType.Application.Json)
            setBody(Fixtures.Notes.Domain.dinner)
        }

        val note = response.body<Note>()

        assertEquals(HttpStatusCode.Created, response.status)
        assertNotNull(note.id)
        assertEquals(Fixtures.Notes.Domain.dinner.title, note.title)
        assertEquals(Fixtures.Notes.Domain.dinner.description, note.description)
        assertNotNull(note.createdAt)
        assertNotNull(note.updatedAt)
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`(): Unit = runBlocking {
        val response = client.get(Routes.Note.MAIN) {
            contentType(ContentType.Application.Json)
        }

        val notes = response.body<List<Note>>()

        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(notes)
        assertTrue(notes.isEmpty())
    }

    @Test
    fun `should return a list with one note`(): Unit = runBlocking {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.get(Routes.Note.MAIN) {
            contentType(ContentType.Application.Json)
        }

        val notes = response.body<List<Note>>()

        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(notes)
        assertTrue(notes.isNotEmpty())
        assertEquals(1, notes.size)
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

        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(notes)
        assertTrue(notes.isNotEmpty())
        assertEquals(10, notes.size)
    }

    // endregion

    // region read delete

    @Test
    fun `should not delete a note and throw bad-request with invalid id`(): Unit = runBlocking {
        postNote(Fixtures.Notes.Domain.dinner)

        val response = client.delete(Routes.Note.DETAIL + 100)

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `should delete a note with valid id`(): Unit = runBlocking {
        val id = postNote(Fixtures.Notes.Domain.dinner)

        val response = client.delete(Routes.Note.DETAIL + id)

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    // endregion

    private suspend fun postNote(note: Note): Int {
        return client.post(Routes.Note.MAIN) {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body<Note>().id!!
    }
}