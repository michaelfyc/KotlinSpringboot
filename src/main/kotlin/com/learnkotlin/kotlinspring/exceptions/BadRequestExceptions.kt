package com.learnkotlin.kotlinspring.exceptions

open class BadRequestException(override val code: Int = 401, override val message: String = "parameters invalid") :
    CommonExceptions(code, message)

class DuplicatedEmailException(override val code: Int = 4001, override val message: String = "email duplicated") :
    BadRequestException(code, message)
