package com.learnkotlin.kotlinspring.controller

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.enums.CommonRoles
import com.learnkotlin.kotlinspring.enums.DEFAULT_TIME_STRING
import com.learnkotlin.kotlinspring.exceptions.WrongCredentialException
import com.learnkotlin.kotlinspring.service.impl.UserServiceImpl
import com.learnkotlin.kotlinspring.util.ResultVO
import com.learnkotlin.kotlinspring.util.StatusOK
import com.learnkotlin.kotlinspring.util.annotations.NeedRole
import com.learnkotlin.kotlinspring.util.jwt.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
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
        logger.info("${user.email} signing up as ${user.role} successfully")
        return ResultVO(StatusOK("sign up successfully"), mapOf("uid" to uid))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody user: User): ResultVO {
        val email = user.email
        val password = user.password
        logger.info("Signing in:${user.email} ${user.password}")
        val userByEmail = userServiceImpl.getUserByEmail(email)
        if (userByEmail?.lockTo!!.isAfter(LocalDateTime.now())) {
            userServiceImpl.setUserLockStatus(userByEmail.uid, false)
        }
        if (userByEmail.password == password && !userByEmail.isLocked) {
            val token = JwtUtils.sign(userByEmail)
            logger.info("${user.email} signed in as ${user.role} successfully")
            return ResultVO(StatusOK("login successfully"), mapOf("token" to token))
        }
        return ResultVO(WrongCredentialException())
    }

    @PutMapping("/lock")
    @NeedRole(CommonRoles.ADMIN)
    fun setUserLockStatus(
        uid: Int,
        lock: Boolean,
        @RequestParam(required = false, defaultValue = DEFAULT_TIME_STRING) lockTo: LocalDateTime
    ): ResultVO {
        logger.info("setting $uid state")
        userServiceImpl.setUserLockStatus(uid, lock, lockTo)
        val state = if (lock) "locked" else "unlocked"
        return ResultVO(StatusOK("user $uid $state successfully"))
    }

    @GetMapping("/users")
    @NeedRole(CommonRoles.ADMIN)
    fun getAllUsers(
        @RequestParam(required = false, defaultValue = "USER") role: CommonRoles,
        @RequestParam(value = "page_num", required = false, defaultValue = "1") pageNum: Int,
        @RequestParam(value = "page_size", required = false, defaultValue = "15") pageSize: Int
    ): ResultVO {
        PageHelper.startPage<User>(pageNum, pageSize)
        val users = userServiceImpl.listUsers(role)
        val usersAfterPaging = PageInfo(users)
        val result = mapOf(
            "users" to usersAfterPaging.list,
            "current_page" to usersAfterPaging.pageNum,
            "total" to usersAfterPaging.total,
            "has_next_page" to usersAfterPaging.isHasNextPage,
            "has_previous_page" to usersAfterPaging.isHasPreviousPage
        )
        return ResultVO(StatusOK(), result)
    }
}
