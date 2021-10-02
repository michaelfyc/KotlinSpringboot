package com.learnkotlin.kotlinspring.enums

import com.baomidou.mybatisplus.annotation.EnumValue

enum class CommonRoles(@EnumValue val rid: Int, @EnumValue val roleName: String) {
    ROOT(1, "root"),
    ADMIN(2, "admin"),
    USER(3, "user")
}
