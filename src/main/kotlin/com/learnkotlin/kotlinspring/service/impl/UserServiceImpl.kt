package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.mapper.UserMapper
import com.learnkotlin.kotlinspring.service.IUserService
import com.learnkotlin.kotlinspring.util.DuplicatedEmailException
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
        return userMapper.selectById(uid)
    }

    override fun getUserByEmail(email: String): User? {
        logger.info("<UserServiceImpl.getUserByEmail>Get user with email:$email")
        return userMapper.getUserByEmail(email)
    }

    override fun createUser(user: User): Int {
        logger.info("<UserServiceImpl.createUser>Create user ${user.username}")
        val status: Int
        try {
            userMapper.insert(user)
            status = user.uid
        } catch (e: DuplicateKeyException) {
            throw DuplicatedEmailException()
        } catch (e: Exception) {
            throw e
        }
        return status
    }

    override fun batchCreateUser(users: HashSet<User>) {
        userMapper.batchCreateUser(users)
    }
}
