package com.learnkotlin.kotlinspring.util.annotations

import com.learnkotlin.kotlinspring.enums.CommonRoles

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NeedRole(val role: CommonRoles = CommonRoles.USER)
