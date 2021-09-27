package com.learnkotlin.kotlinspring.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.learnkotlin.kotlinspring.entity.User
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper : BaseMapper<User> {
    fun batchCreateUser(users: HashSet<User>): Boolean

    fun getUserByEmail(email: String): User?
}