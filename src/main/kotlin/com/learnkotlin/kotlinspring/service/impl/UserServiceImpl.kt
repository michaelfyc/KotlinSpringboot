package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.enums.CommonRoles
import com.learnkotlin.kotlinspring.exceptions.DuplicatedEmailException
import com.learnkotlin.kotlinspring.mapper.UserMapper
import com.learnkotlin.kotlinspring.service.IUserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

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
}
