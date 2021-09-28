package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.util.DuplicatedEmailException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
internal class UserServiceImplTest {

    private val userJohn = User(username = "John", password = "123456", email = "john@example.com")
    private val userAndy = User(username = "Andy", password = "654321", email = "andy@example.com")
    private val users = HashSet<User>()

    init {
        users.add(userAndy)
        users.add(userJohn)
    }

    @Autowired
    lateinit var userServiceImpl: UserServiceImpl

    @Test
    @Transactional
    fun testCreateUser() {
        userServiceImpl.createUser(userAndy)
        val newUser = userServiceImpl.getUserByEmail(userAndy.email)
        assertNotNull(newUser)
        assertEquals(userAndy.password, newUser?.password)
        assertEquals(userAndy.isLocked, newUser?.isLocked)
        // 重复插入
        var status = -1
        try {
            status = userServiceImpl.createUser(userAndy)
        } catch (e: Exception) {
            assertEquals(-1, status)
            assertTrue(e is DuplicatedEmailException)
        }
    }

    @Test
    @Transactional
    fun testGetUserByUid() {
        val uid = userServiceImpl.createUser(userAndy)
        assertNotEquals(0, uid)
        val user = userServiceImpl.getUserByUid(userAndy.uid)
        assertNotNull(user)
        assertEquals(userAndy.email, user!!.email)
        assertEquals(userAndy.password, user.password)
        assertEquals(userAndy.isLocked, user.isLocked)
    }

    @Test
    fun testGetUserByEmail() {
        val email = "haha@example.com"
        val user = userServiceImpl.getUserByEmail(email)
        assertNull(user)
    }
}
