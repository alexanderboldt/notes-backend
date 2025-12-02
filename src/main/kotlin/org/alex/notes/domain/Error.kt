package org.alex.notes.domain

import kotlinx.serialization.Serializable

@Serializable
data class Error(val message: String)
