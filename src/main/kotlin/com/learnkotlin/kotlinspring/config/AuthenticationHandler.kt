package com.learnkotlin.kotlinspring.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.learnkotlin.kotlinspring.exceptions.InvalidTokenException
import com.learnkotlin.kotlinspring.exceptions.PermissionDeniedException
import com.learnkotlin.kotlinspring.exceptions.WrongCredentialException
import com.learnkotlin.kotlinspring.service.impl.UserServiceImpl
import com.learnkotlin.kotlinspring.util.annotations.NeedRole
import com.learnkotlin.kotlinspring.util.jwt.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationHandler : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        val methods = handler.method
        // check if the method is annotated by @NeedRole
        if (methods.isAnnotationPresent(NeedRole::class.java)) {
            val role = methods.getAnnotation(NeedRole::class.java).role
            val auth = request.getHeader("Authorization")
                ?: throw InvalidTokenException(message = "please sign in first to visit")
            val token = auth.replace("Bearer ", "")
            val uid = JwtUtils.getUid(token)
            val user = userServiceImpl.getUserByUid(uid) ?: throw WrongCredentialException()
            val verifier = JWT.require(Algorithm.HMAC256(user.password)).build()
            try {
                verifier.verify(token)
            } catch (e: TokenExpiredException) {
                throw com.learnkotlin.kotlinspring.exceptions.TokenExpiredException(message = "token expired")
            } catch (e: JWTVerificationException) {
                throw InvalidTokenException(message = "token invalid")
            }
            // if user lock expires, unlock him or her
            val now = LocalDateTime.now()
            if (user.lockTo.isAfter(now)) {
                userServiceImpl.setUserLockStatus(uid, false)
            }
            // rid 越小角色权限越大，只要角色权限大于等于需要的权限即可
            // 同时还需要用户不被锁定
            if (user.role.rid > role.rid || user.isLocked) {
                throw PermissionDeniedException()
            }
            return true
        }
        return true
    }
}
