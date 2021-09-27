package com.learnkotlin.kotlinspring.util

open class Result<T : Any?>(var code: Int, var data: T?, var message: String) {
    constructor(status: Status, data: T?) : this(code = status.code, message = status.message, data = data)

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
