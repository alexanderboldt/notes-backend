package com.alex.main.kotlin.utils

import io.ktor.server.application.*

val ApplicationCall.clientSecretHeader: String?
    get() = request.headers["Client-Secret"]