package com.alex.main.kotlin.repository

class NotesRepository {

    private val notes = arrayListOf<Note>()

    // -----------------------------------------------------------------------------

    init {
        // seeds
        notes.addAll(listOf(
            Note(1, "Einkaufen", "Das Gemüse nicht vergessen"),
            Note(2, "Aufräumen", "Grundreinigung"),
            Note(3, "Abwaschen", null),
            Note(4, "Tanken", "Diesel"),
            Note(4, "Blumen gießen", "Den Dünger nicht vergessen")))
    }

    // -----------------------------------------------------------------------------

    // create
    fun save(note: Note) = notes.add(note.apply { id = notes.size + 1 })

    // read
    fun getAll(limit: Int? = null) = if (limit != null) notes.take(limit) else notes
    fun get(id: Int) = notes.firstOrNull { it.id == id }

    // update
    fun update(note: Note): Boolean {
        return notes
            .indexOfFirst { it.id == note.id }
            .also { if (it != -1) notes[it] = note }
            .let { it != -1 }
    }

    // delete
    fun delete(): Boolean {
        notes.clear()
        return true
    }

    fun delete(note: Note) = delete(note.id)
    fun delete(id: Int?): Boolean {
        return notes
            .indexOfFirst { it.id == id }
            .also { if (it != -1) notes.removeAt(it)}
            .let { it != -1 }
    }
}