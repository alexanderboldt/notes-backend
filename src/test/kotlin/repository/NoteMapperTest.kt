package com.alex.repository

import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import kotlin.test.Test

class NoteMapperTest {

    @Test
    fun `should map domain to entity`() {
        val domain = Fixtures.Notes.Domain.dinner
        val entity = domain.toEntity()

        expectThat(entity.id).isEqualTo(0)
        expectThat(entity.title).isEqualTo(domain.title)
        expectThat(entity.description).isEqualTo(domain.description)
        expectThat(entity.createdAt).isNotEqualTo(0)
        expectThat(entity.updatedAt).isNotEqualTo(0)
    }

    @Test
    fun `should map entity to domain`() {
        val entity = Fixtures.Notes.Entity.dinner
        val domain = entity.toDomain()

        expectThat(domain.id).isEqualTo(entity.id)
        expectThat(domain.title).isEqualTo(entity.title)
        expectThat(domain.description).isEqualTo(entity.description)
        expectThat(domain.createdAt).isEqualTo(entity.createdAt)
        expectThat(domain.updatedAt).isEqualTo(entity.updatedAt)
    }
}