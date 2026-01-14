package org.alex.notes.utils

import io.ktor.server.plugins.BadRequestException

class BadRequestThrowable : BadRequestException("Invalid input!")
