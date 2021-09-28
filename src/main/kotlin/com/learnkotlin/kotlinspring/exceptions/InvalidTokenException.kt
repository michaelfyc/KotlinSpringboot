package com.learnkotlin.kotlinspring.exceptions

open class InvalidTokenException(override val code: Int = 5001, override val message: String = "invalid token error") :
    CredentialRelatedException(code, message)

class TokenExpiredException(override val code: Int = 5002, override val message: String = "token expired") :
    InvalidTokenException(code, message)
