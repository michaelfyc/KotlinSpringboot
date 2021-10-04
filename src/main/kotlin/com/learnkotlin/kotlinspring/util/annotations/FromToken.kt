package com.learnkotlin.kotlinspring.util.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class FromToken(val field: String)
