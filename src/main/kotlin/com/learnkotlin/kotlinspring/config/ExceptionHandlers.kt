package com.learnkotlin.kotlinspring.config

import com.learnkotlin.kotlinspring.exceptions.BadRequestException
import com.learnkotlin.kotlinspring.exceptions.CommonExceptions
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
        e.printStackTrace()
        val fieldErrors = e.bindingResult.fieldErrors
        val errorMessages = fieldErrors.map { it.defaultMessage }.toList().joinToString(";")
        return ResultVO(BadRequestException(message = errorMessages))
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun illegalArgumentExceptionHandler(e: IllegalArgumentException): ResultVO<Any> {
        e.printStackTrace()
        return ResultVO(BadRequestException(message = e.message!!))
    }

    @ExceptionHandler(value = [CommonExceptions::class])
    fun commonExceptionHandler(e: CommonExceptions): ResultVO<Any> {
        e.printStackTrace()
        return ResultVO(e)
    }
}
