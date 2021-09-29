package com.learnkotlin.kotlinspring.exceptions

open class CredentialRelatedException(override val code: Int = 403, override val message: String = "forbidden") :
    CommonExceptions(code, message)

class WrongCredentialException(override val code: Int = 403, override val message: String = "credential invalid") :
    CredentialRelatedException(code, message)
