package com.learnkotlin.kotlinspring.controller

import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.exceptions.WrongCredentialException
import com.learnkotlin.kotlinspring.service.impl.UserServiceImpl
import com.learnkotlin.kotlinspring.util.ResultVO
import com.learnkotlin.kotlinspring.util.StatusOK
import com.learnkotlin.kotlinspring.util.jwt.JwtUtils
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
    private lateinit var userServiceImpl: UserServiceImpl

    @PostMapping("/register")
    fun register(@Valid @RequestBody user: User): ResultVO {
        logger.info("Registering:${user.email} ${user.username} ${user.password}")
        val uid = userServiceImpl.createUser(user)
        return ResultVO(StatusOK("sign up successfully"), mapOf("uid" to uid))
    }

    @PostMapping("/login")
    fun login(@RequestBody user: User): ResultVO {
        val email = user.email
        val password = user.password
        logger.info("Signing in:${user.email} ${user.password}")
        val userByEmail = userServiceImpl.getUserByEmail(email)
        if (userByEmail?.password == password && !userByEmail.isLocked) {
            val token = JwtUtils().sign(userByEmail)
            return ResultVO(StatusOK("login successfully"), mapOf("token" to token))
        }
        return ResultVO(WrongCredentialException())
    }
}
