package com.learnkotlin.kotlinspring.config


import com.learnkotlin.kotlinspring.util.Result
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ExceptionHandlers {
    /**
     * Handle Hibernate Validation Exceptions
     */
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun invalidMethodParameterExceptionHandler(e: MethodArgumentNotValidException): Result<Any> {
        val fieldErrors = e.bindingResult.fieldErrors
        val errorMessages = fieldErrors.map { it.defaultMessage }.toList().joinToString(";")
        return Result(code = 4001, data = null, message = errorMessages)
    }
}