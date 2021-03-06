package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.enums.CommonRoles
import com.learnkotlin.kotlinspring.enums.DEFAULT_TIME
import com.learnkotlin.kotlinspring.exceptions.DuplicatedEmailException
import com.learnkotlin.kotlinspring.mapper.UserMapper
import com.learnkotlin.kotlinspring.service.IUserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl : IUserService {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var userMapper: UserMapper

    override fun getUserByUid(uid: Int): User? {
        logger.info("<UserServiceImpl.getUserByUid>Get user with uid:$uid")
        val user = userMapper.selectById(uid)
        user.role = userMapper.getUserRole(uid)
        return user
    }

    override fun getUserByEmail(email: String): User? {
        logger.info("<UserServiceImpl.getUserByEmail>Get user with email:$email")
        val user = userMapper.getUserByEmail(email)
        if (user != null) {
            user.role = userMapper.getUserRole(user.uid)
        }
        return user
    }

    override fun createUser(user: User, role: CommonRoles): Int {
        logger.info("<UserServiceImpl.createUser>Create user ${user.username}")
        val status: Int
        try {
            userMapper.insert(user)
            userMapper.assignRole(user.uid, role)
            status = user.uid
        } catch (e: DuplicateKeyException) {
            throw DuplicatedEmailException()
        } catch (e: Exception) {
            throw e
        }
        return status
    }

    override fun listUsers(role: CommonRoles): List<User> {
        logger.info("<UserServiceImpl.listUsers>role:$role")
        return userMapper.listUsersByRole(role) ?: emptyList()
    }

    override fun setUserLockStatus(uid: Int, lock: Boolean, lockTo: LocalDateTime) {
        logger.info("<UserServiceImpl.setUserLockStatus>uid: $uid, lock $lock")
        userMapper.setUserLockStatus(uid, lock)
        if (lock) {
            userMapper.setUserLockTime(uid, lockTo)
        } else {
            userMapper.setUserLockTime(uid, DEFAULT_TIME)
        }
    }
}
