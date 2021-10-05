package com.learnkotlin.kotlinspring.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.enums.CommonRoles
import org.apache.ibatis.annotations.Mapper
import java.time.LocalDateTime

@Mapper
interface UserMapper : BaseMapper<User> {
    fun getUserByEmail(email: String): User?

    fun setUserRole(uid: Int, role: CommonRoles): Boolean

    fun getUserRole(uid: Int): CommonRoles

    fun listUsersByRole(roles: CommonRoles): List<User>?

    fun assignRole(uid: Int, role: CommonRoles)

    fun setUserLockStatus(uid: Int, lock: Boolean)

    fun setUserLockTime(uid: Int, lockTo: LocalDateTime)
}
