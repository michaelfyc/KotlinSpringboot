package com.learnkotlin.kotlinspring.service

import com.learnkotlin.kotlinspring.entity.User
import java.lang.Exception

interface IUserService {

    fun getUserByUid(uid: Int): User?

    fun getUserByEmail(email: String): User?

    /**
     * createUser
     * 新建用户，并返回 uid
     */
    fun createUser(user: User): Int

    fun batchCreateUser(users: HashSet<User>)

}