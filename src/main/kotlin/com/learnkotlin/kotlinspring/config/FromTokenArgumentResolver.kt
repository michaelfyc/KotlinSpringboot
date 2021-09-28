package com.learnkotlin.kotlinspring.config

import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.exceptions.InvalidTokenException
import com.learnkotlin.kotlinspring.util.annotations.FromToken
import com.learnkotlin.kotlinspring.util.jwt.JwtUtils
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest
import kotlin.IllegalArgumentException

class FromTokenArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(methodParameter: MethodParameter): Boolean {
        return methodParameter.hasParameterAnnotation(FromToken::class.java)
    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        modelAndViewContainer: ModelAndViewContainer?,
        nativeWebRequest: NativeWebRequest,
        webDataBinderFactory: WebDataBinderFactory?
    ): Any {
        val request = nativeWebRequest.nativeRequest as HttpServletRequest
        val fromToken = methodParameter.getParameterAnnotation(FromToken::class.java)
            ?: throw IllegalArgumentException("FromToken Annotation cannot resolve ${methodParameter.parameterType.name}")

        val authHeader = request.getHeader("authorization")
        val token = authHeader.replace("Bearer ", "")
        // fetch field from @FromToken(field) 
        val field = fromToken.field
        return getCastedClaim(field, token)
    }
}

// getCastedClaim 获取 claim 并进行类型转换
fun getCastedClaim(field: String, token: String): Any {
    val jwtUtils = JwtUtils()
    val result =
        when (field) {
            "uid" -> jwtUtils.getUid(token)
            "email" -> jwtUtils.getEmail(token)
            "username" -> jwtUtils.getUsername(token)
            "isLocked" -> jwtUtils.getIsLocked(token)
            "all" -> {
                val uid = jwtUtils.getUid(token)
                val email = jwtUtils.getEmail(token)
                val username = jwtUtils.getUsername(token)
                val isLocked = jwtUtils.getIsLocked(token)
                User(uid = uid, email = email, username = username, isLocked = isLocked, password = "")
            }
            else -> throw InvalidTokenException(message = "No such field in token")
        }
    return result
}
