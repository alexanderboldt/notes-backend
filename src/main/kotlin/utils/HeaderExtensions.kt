package com.alex.utils

import io.ktor.server.application.*

val ApplicationCall.clientSecretHeader: String?
    get() = request.headers["Client-Secret"]