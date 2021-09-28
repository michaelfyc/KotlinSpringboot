package com.learnkotlin.kotlinspring.exceptions

sealed class CommonExceptions(open val code: Int, override val message: String) : Exception(message)

class ServerException(override val code: Int = 500, override val message: String = "internal server error") :
    CommonExceptions(code, message)
