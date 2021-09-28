package com.learnkotlin.kotlinspring.config

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import com.learnkotlin.kotlinspring.exceptions.InvalidTokenException
import com.learnkotlin.kotlinspring.exceptions.TokenClaimNotFoundException
import com.learnkotlin.kotlinspring.util.annotations.FromToken
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserInfoHandler : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authHeader = request.getHeader("authorization")
        val token = authHeader.replace("Bearer ", "")
        if (handler !is HandlerMethod) {
            return true
        }
        val methods = handler.method
        if (methods.isAnnotationPresent(FromToken::class.java)) {
            val decodedJWT: DecodedJWT
            // decode JWT token first
            try {
                decodedJWT = JWT.decode(token)
            } catch (e: JWTDecodeException) {
                throw InvalidTokenException(message = "invalid token")
            }
            val field = methods.getAnnotation(FromToken::class.java).field
            val claim = decodedJWT.getClaim(field)
                ?: throw TokenClaimNotFoundException(message = "token claim $field not found")
            request.setAttribute(field, claim)
        }
        return super.preHandle(request, response, handler)
    }
}
