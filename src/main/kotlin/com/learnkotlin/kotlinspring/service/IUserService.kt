package com.learnkotlin.kotlinspring.service

import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.enums.CommonRoles
import com.learnkotlin.kotlinspring.enums.DEFAULT_TIME
import java.time.LocalDateTime

interface IUserService {

    /**
     * getUserByEmail 根据 uid 获取 user
     * @param uid 用户 uid
     * @return User 用户对象
     */
    fun getUserByUid(uid: Int): User?

    /**
     * getUserByUid 根据 email 获取 user
     * @param email 用户邮箱
     * @return User 用户对象
     */
    fun getUserByEmail(email: String): User?

    /**
     * createUser 新建用户，并返回 uid
     * @param user 用户对象
     * @param role 角色
     * @return Int 新建用户 uid
     */
    fun createUser(user: User, role: CommonRoles = CommonRoles.USER): Int

    /**
     * listUsers 查询特定角色的用户列表
     * @param role 角色
     * @return List<User> 用户列表
     */
    fun listUsers(role: CommonRoles = CommonRoles.USER): List<User>

    /**
     * setUserLockStatus 设置用户禁用状态
     * @param uid 用户 uid
     * @param lock 锁定: true, 解锁: false
     * @param lockTo 锁定至，仅当 lock 为 true 时有意义
     */
    fun setUserLockStatus(uid: Int, lock: Boolean, lockTo: LocalDateTime = DEFAULT_TIME)
}
