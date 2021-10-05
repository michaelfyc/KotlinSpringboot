package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.enums.DEFAULT_TIME
import com.learnkotlin.kotlinspring.exceptions.DuplicatedEmailException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
internal class UserServiceImplTest {

    private lateinit var userJohn: User
    private lateinit var userAndy: User
    private lateinit var lockTo: LocalDateTime

    @Autowired
    lateinit var userServiceImpl: UserServiceImpl

    @BeforeEach
    fun setup() {
        lockTo = LocalDateTime.now().plusMinutes(1).withNano(0)
        userAndy = User(username = "Andy", password = "654321", email = "andy@example.com")
        userJohn =
            User(
                username = "John",
                password = "123456",
                email = "john@example.com",
                isLocked = true,
                lockTo = lockTo
            )
    }

    @Test
    @Transactional
    fun testCreateUser_0() {
        userServiceImpl.createUser(userAndy)
        val newUser = userServiceImpl.getUserByEmail(userAndy.email)
        assertNotNull(newUser)
        assertEquals(userAndy.password, newUser?.password)
        assertEquals(userAndy.isLocked, newUser?.isLocked)
        assertEquals(userAndy.role, newUser?.role)
    }

    @Test
    @Transactional
    fun testCreateUser_1() {
        testCreateUser_0()
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
    fun testSetUserLockStatus_0() {
        // user wasn't locked
        val uid = userServiceImpl.createUser(userAndy)
        assertTrue(uid > 0)
        userServiceImpl.setUserLockStatus(uid, true, lockTo)
        val userAndyAfterLocked = userServiceImpl.getUserByUid(uid)
        assertNotNull(userAndyAfterLocked)
        assertTrue(userAndyAfterLocked!!.isLocked)
        assertEquals(lockTo, userAndyAfterLocked.lockTo)
    }

    @Test
    @Transactional
    fun testSetUserLockStatus_1() {
        // user was locked
        val uid = userServiceImpl.createUser(userJohn)
        assertTrue(uid > 0)
        userServiceImpl.setUserLockStatus(uid, false)
        val userJohnAfterLocked = userServiceImpl.getUserByUid(uid)
        assertNotNull(userJohnAfterLocked)
        assertFalse(userJohnAfterLocked!!.isLocked)
        assertEquals(DEFAULT_TIME.withNano(0), userJohnAfterLocked.lockTo.withNano(0))
    }

    @Test
    @Transactional
    fun testGetUserByUid_0() {
        val uid = userServiceImpl.createUser(userAndy)
        assertTrue(uid > 0)
        val user = userServiceImpl.getUserByUid(userAndy.uid)
        assertNotNull(user)
        assertEquals(userAndy.email, user!!.email)
        assertEquals(userAndy.password, user.password)
        assertEquals(userAndy.isLocked, user.isLocked)
        assertEquals(userAndy.role, user.role)
    }

    @Test
    @Transactional
    fun testGetUserByUid_1() {
        val uid = userServiceImpl.createUser(userJohn)
        assertTrue(uid > 0)
        // getUserByUid does not care whether the user is locked
        val user = userServiceImpl.getUserByUid(uid)
        assertNotNull(user)
    }

    @Test
    fun testGetUserByEmail() {
        val email = "haha@example.com"
        val user = userServiceImpl.getUserByEmail(email)
        assertNull(user)
    }
}
