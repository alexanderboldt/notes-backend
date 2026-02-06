package org.alex.notes.repository

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.alex.notes.Fixtures

class NoteMapperUnitTest : StringSpec ({
    
    "should map domain to entity" {
        val domain = Fixtures.Notes.Domain.dinner
        val entity = domain.toEntity()

        entity.id shouldBe 0
        entity.title shouldBe domain.title
        entity.description shouldBe domain.description
        entity.filename.shouldBeNull()
        entity.createdAt shouldBeGreaterThan  0
        entity.updatedAt shouldBeGreaterThan 0
    }

    "should map entity to domain" {
        val entity = Fixtures.Notes.Entity.dinner
        val domain = entity.toDomain()

        domain.id shouldBe entity.id
        domain.title shouldBe entity.title
        domain.description shouldBe entity.description
        domain.filename.shouldBeNull()
        domain.createdAt shouldBe entity.createdAt
        domain.updatedAt shouldBe entity.updatedAt
    }
})
