package com.learnkotlin.kotlinspring.controller

import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.service.impl.UserServiceImpl
import com.learnkotlin.kotlinspring.util.DuplicationEmailException
import com.learnkotlin.kotlinspring.util.Result
import com.learnkotlin.kotlinspring.util.ServerException
import com.learnkotlin.kotlinspring.util.StatusOK
import com.learnkotlin.kotlinspring.util.WrongCredentialException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class UserController {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var userServiceImpl: UserServiceImpl

    @PostMapping("/register")
    fun register(@Valid @RequestBody user: User): Result<Any> {
        logger.info("Registering:${user.email} ${user.username} ${user.password}")
        val uid: Int
        try {
            uid = userServiceImpl.createUser(user)
        } catch (e: DuplicationEmailException) {
            return Result(e)
        } catch (e: Exception) {
            return Result(ServerException())
        }
        return Result(StatusOK("sign up successfully"), mapOf("uid" to uid))
    }

    @PostMapping("/login")
    fun login(@RequestBody user: User): Result<Any> {
        val email = user.email
        val password = user.password
        logger.info("Signing in:${user.email} ${user.password}")
        val userByEmail = userServiceImpl.getUserByEmail(email)
        if (userByEmail != null) {
            if (userByEmail.password == password && !userByEmail.isLocked) {
                return Result(StatusOK("login successfully"), userByEmail)
            }
        }
        return Result(WrongCredentialException())
    }
}
