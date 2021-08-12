package com.alex.main.kotlin.repository

import com.alex.main.kotlin.feature.Notes
import kotlin.collections.ArrayList

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
            Note(5, "Blumen gießen", "Den Dünger nicht vergessen")))
    }

    // -----------------------------------------------------------------------------

    // create
    fun save(note: Note) = notes.add(note.apply { id = notes.size + 1 })

    // read
    fun getAll(offset: Int? = null, limit: Int? = null): List<Note> {
        return notes
            .drop(if (offset != null && offset >= 0) offset else 0)
            .let { notes -> if (limit != null) notes.take(limit) else notes }
    }

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