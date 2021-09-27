package com.learnkotlin.kotlinspring.util

sealed class Status(open val code: Int, open val message: String)

data class StatusOK(override val message: String = "OK") : Status(code = 200, message = message)

data class StatusForbidden(override val message: String) : Status(code = 403, message = message)

data class StatusBadRequest(override val message: String) : Status(code = 401, message = message)

data class StatusNotFound(override val message: String) : Status(code = 404, message = message)
