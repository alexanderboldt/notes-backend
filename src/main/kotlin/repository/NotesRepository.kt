package com.alex.main.kotlin.repository

class NotesRepository {

    private val notes = arrayListOf<Note>()

    // -----------------------------------------------------------------------------

    fun get() = notes
    fun get(id: Int) = notes.firstOrNull { it.id == id }

    fun save(note: Note) = notes.add(note.apply { id = notes.size + 1 })

    fun update(note: Note): Boolean {
        return notes
            .indexOfFirst { it.id == note.id }
            .also { if (it != -1) notes[it] = note }
            .let { it != -1 }
    }

    fun delete(note: Note) = delete(note.id)
    fun delete(id: Int?): Boolean {
        return notes
            .indexOfFirst { it.id == id }
            .also { if (it != -1) notes.removeAt(it)}
            .let { it != -1 }
    }
}