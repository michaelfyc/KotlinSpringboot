package com.learnkotlin.kotlinspring.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.learnkotlin.kotlinspring.enums.CommonRoles
import org.apache.ibatis.annotations.AutomapConstructor
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@TableName("user")
data class User @AutomapConstructor constructor(
    @TableId(type = IdType.AUTO)
    var uid: Int = 0,
    var username: String = "",
    @field:Size(min = 6, max = 30, message = "password length must be between 6 and 30")
    var password: String,
    @field:Email(message = "invalid email address")
    var email: String,
    var isLocked: Boolean = false,
) {
    @TableField(exist = false)
    var role: CommonRoles = CommonRoles.USER
}
