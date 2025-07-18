package org.alex.notes

import org.alex.notes.domain.Note
import org.alex.notes.repository.NoteEntity

object Fixtures {
    object Notes {
        object Domain {
            val shopping = Note(10, "Groceries", "For one week", 17234824, 17234824)
            val dinner = Note(10, "Make Dinner", "Healthy", 17234824, 17234824)
        }
        object Entity {
            val dinner = NoteEntity(10, "Make Dinner", "Healthy", 17234824, 17234824)
        }
    }
}
