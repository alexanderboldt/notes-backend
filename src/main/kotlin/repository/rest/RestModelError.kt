package com.alex.repository.rest

import kotlinx.serialization.Serializable

@Serializable
data class RestModelError(val message: String)