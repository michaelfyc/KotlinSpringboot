package com.learnkotlin.kotlinspring.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.learnkotlin.kotlinspring.service.impl.UserServiceImpl
import com.learnkotlin.kotlinspring.util.InvalidTokenException
import com.learnkotlin.kotlinspring.util.WrongCredentialException
import com.learnkotlin.kotlinspring.util.annotations.NeedAuthorized
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
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
        if (methods.isAnnotationPresent(NeedAuthorized::class.java)) {
            val auth = request.getHeader("Authorization") ?: throw InvalidTokenException(message = "please sign in first to visit")
            var token = auth
            if (auth.startsWith("Bearer ")) {
                token = auth.substring(7, auth.length)
            }
            val uid: Int
            try {
                uid = JWT.decode(token).audience[0].toInt()
            } catch (e: JWTDecodeException) {
                throw InvalidTokenException()
            }
            val user = userServiceImpl.getUserByUid(uid) ?: throw WrongCredentialException()
            val verifier = JWT.require(Algorithm.HMAC256(user.password)).build()
            try {
                verifier.verify(token)
            } catch (e: TokenExpiredException) {
                throw InvalidTokenException(message = "token expired")
            } catch (e: JWTVerificationException) {
                throw RuntimeException("token invalid")
            }
            return true
        }
        return true
    }
}
