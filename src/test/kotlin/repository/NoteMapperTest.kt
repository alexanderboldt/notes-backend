package repository

import com.alex.domain.Note
import com.alex.repository.NoteEntity
import com.alex.repository.toDomain
import com.alex.repository.toEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class NoteMapperTest {

    @Test
    fun `should map rest to database`() {
        val domain = Note(0, "Make Dinner", "Healthy", 17234824, 17234824)
        val entity = domain.toEntity()

        assertEquals(0, entity.id)
        assertEquals(domain.title, entity.title)
        assertEquals(domain.description, entity.description)
        assertNotEquals(0, entity.createdAt)
        assertNotEquals(0, entity.updatedAt)
    }

    @Test
    fun `should map database to reset`() {
        val entity = NoteEntity(10, "Make Dinner", "Healthy", 17234824, 17234824)
        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.title, domain.title)
        assertEquals(entity.description, domain.description)
        assertEquals(entity.createdAt, domain.createdAt)
        assertEquals(entity.updatedAt, domain.updatedAt)
    }
}