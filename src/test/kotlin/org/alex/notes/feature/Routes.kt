package org.alex.notes.feature

object Routes {
    const val ROOT = "/api"

    object Note {
        const val MAIN = "/api/v1/notes"
        const val DETAIL = "$MAIN/"
    }
}
