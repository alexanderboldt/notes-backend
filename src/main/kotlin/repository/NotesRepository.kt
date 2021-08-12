package com.alex.main.kotlin.repository

import java.util.*

class NotesRepository {

    private val notes = arrayListOf<Note>()

    // -----------------------------------------------------------------------------

    init {
        // seeds
        val date = Date().time
        
        notes.addAll(listOf(
            Note(1, "Einkaufen", "Gemüse nicht vergessen", date, date),
            Note(2, "Aufräumen", "Grundreinigung", date, date),
            Note(3, "Abwaschen", null, date, date),
            Note(4, "Tanken", "Diesel", date, date),
            Note(5, "Blumen gießen", "Dünger nicht vergessen", date, date)))
    }

    // -----------------------------------------------------------------------------

    // create
    fun save(note: Note) {
        val date = Date().time

        note.apply {
            id = notes.size + 1
            createdAt = date
            updatedAt = date
        }.apply { notes.add(this) }
    }

    // read
    fun getAll(sort: Pair<String,Boolean>? = null, offset: Int? = null, limit: Int? = null): List<Note> {
        return notes
            .apply {
                when (sort?.first) {
                    "id" -> if (sort.second) sortBy { it.id } else sortByDescending { it.id }
                    "title" -> if (sort.second) sortBy { it.title } else sortByDescending { it.title }
                    "description" -> if (sort.second) sortBy { it.description } else sortByDescending { it.description }
                }
            }.drop(if (offset != null && offset >= 0) offset else 0)
            .run { if (limit != null) take(limit) else this }
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

    fun delete(id: Int?): Boolean {
        return notes
            .indexOfFirst { it.id == id }
            .also { if (it != -1) notes.removeAt(it)}
            .let { it != -1 }
    }
}