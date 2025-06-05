package repository

import com.alex.repository.database.DbModelNote
import com.alex.repository.rest.RestModelNotePost
import com.alex.repository.toDbModel
import com.alex.repository.toRestModelGet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class NoteMapperTest {

    @Test
    fun `should map rest to database`() {
        val noteRest = RestModelNotePost("Make Dinner", "Healthy")
        val noteDb = noteRest.toDbModel()

        assertEquals(0, noteDb.id)
        assertEquals(noteRest.title, noteDb.title)
        assertEquals(noteRest.description, noteDb.description)
        assertNotEquals(0, noteDb.createdAt)
        assertNotEquals(0, noteDb.updatedAt)
    }

    @Test
    fun `should map database to reset`() {
        val noteDb = DbModelNote(10, "Make Dinner", "Healthy", 17234824, 17234824)
        val noteRest = noteDb.toRestModelGet()

        assertEquals(noteDb.id, noteRest.id)
        assertEquals(noteDb.title, noteRest.title)
        assertEquals(noteDb.description, noteRest.description)
        assertEquals(noteDb.createdAt, noteRest.createdAt)
        assertEquals(noteDb.updatedAt, noteRest.updatedAt)
    }
}