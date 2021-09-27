package com.learnkotlin.kotlinspring.config

import com.learnkotlin.kotlinspring.util.ResultVO
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlers {
    /**
     * Handle Hibernate Validation Exceptions
     */
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun invalidMethodParameterExceptionHandler(e: MethodArgumentNotValidException): ResultVO<Any> {
        val fieldErrors = e.bindingResult.fieldErrors
        val errorMessages = fieldErrors.map { it.defaultMessage }.toList().joinToString(";")
        return ResultVO(code = 4001, data = null, message = errorMessages)
    }
}
