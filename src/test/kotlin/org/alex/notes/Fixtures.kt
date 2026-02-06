package org.alex.notes

import org.alex.notes.domain.NoteRequest
import org.alex.notes.repository.NoteEntity

object Fixtures {
    object Notes {
        object Domain {
            val shopping = NoteRequest("Groceries", "For one week")
            val dinner = NoteRequest("Make Dinner", "Healthy")
        }
        object Entity {
            val dinner = NoteEntity(10, "Make Dinner", "Healthy", null, 17234824, 17234824)
        }
    }
}
