package com.alex.repository

import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isNotNull
import kotlin.test.Test

class NoteMapperTest {

    @Test
    fun `should map domain to entity`() {
        val domain = Fixtures.Notes.Domain.dinner
        val entity = domain.toEntity()

        expectThat(entity.id).isEqualTo(0)
        expectThat(entity.title).isEqualTo(domain.title)
        expectThat(entity.description).isEqualTo(domain.description)
        expectThat(entity.createdAt).isGreaterThan(0)
        expectThat(entity.updatedAt).isGreaterThan(0)
    }

    @Test
    fun `should map entity to domain`() {
        val entity = Fixtures.Notes.Entity.dinner
        val domain = entity.toDomain()

        expectThat(domain.id).isNotNull().isEqualTo(entity.id)
        expectThat(domain.title).isEqualTo(entity.title)
        expectThat(domain.description).isNotNull().isEqualTo(entity.description)
        expectThat(domain.createdAt).isNotNull().isEqualTo(entity.createdAt)
        expectThat(domain.updatedAt).isNotNull().isEqualTo(entity.updatedAt)
    }
}