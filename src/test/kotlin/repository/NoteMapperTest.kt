package com.alex.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class NoteMapperTest {

    @Test
    fun `should map domain to entity`() {
        val domain = Fixtures.Notes.Domain.dinner
        val entity = domain.toEntity()

        assertEquals(0, entity.id)
        assertEquals(domain.title, entity.title)
        assertEquals(domain.description, entity.description)
        assertNotEquals(0, entity.createdAt)
        assertNotEquals(0, entity.updatedAt)
    }

    @Test
    fun `should map entity to domain`() {
        val entity = Fixtures.Notes.Entity.dinner
        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.title, domain.title)
        assertEquals(entity.description, domain.description)
        assertEquals(entity.createdAt, domain.createdAt)
        assertEquals(entity.updatedAt, domain.updatedAt)
    }
}