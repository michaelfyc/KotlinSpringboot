package com.learnkotlin.kotlinspring.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import org.apache.ibatis.annotations.AutomapConstructor

@TableName("user_role")
data class UserRole @AutomapConstructor constructor(
    @TableId(type = IdType.AUTO)
    val id: Int,
    val uid: Int,
    val rid: Int
)
