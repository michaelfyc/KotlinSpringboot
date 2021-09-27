package com.learnkotlin.kotlinspring.util

sealed class CommonExceptions(open val code: Int, override val message: String) : Exception(message)

class BadRequestException(override val code: Int = 4002, override val message: String = "parameters invalid") :
    CommonExceptions(code, message)

class WrongCredentialException(override val code: Int = 403, override val message: String = "credential invalid") :
    CommonExceptions(code, message)

class DuplicationEmailException(override val code: Int = 4002, override val message: String = "email duplicated") :
    CommonExceptions(code, message)

class ServerException(override val code: Int = 500, override val message: String = "internal server error") :
    CommonExceptions(code, message)


