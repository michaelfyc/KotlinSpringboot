package com.learnkotlin.kotlinspring.util

import com.learnkotlin.kotlinspring.exceptions.CommonExceptions

/**
 * ResultVO wraps response in standardized json format
 */
open class ResultVO(var code: Int, var data: Any?, var message: String) {
    constructor(status: Status, data: Any? = null) : this(code = status.code, message = status.message, data = data)

    constructor(commonExceptions: CommonExceptions) : this(
        code = commonExceptions.code,
        message = commonExceptions.message,
        data = null
    )

    init {
        require(code > 0) {
            "status code invalid"
        }
    }
}
