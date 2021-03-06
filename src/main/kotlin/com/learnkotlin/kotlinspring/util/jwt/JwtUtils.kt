package com.learnkotlin.kotlinspring.util.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.enums.CommonRoles
import org.slf4j.LoggerFactory
import java.util.Date

object JwtUtils {
    private val log = LoggerFactory.getLogger(this::class.java)

    // 过期时间5分钟
    private const val expireTime = (5 * 60 * 1000).toLong()

    /**
     * 生成签名,5min后过期
     *
     * @param user 用户
     * @return 加密的token
     */
    fun sign(user: User): String {
        val date = Date(System.currentTimeMillis() + expireTime)
        val algorithm: Algorithm = Algorithm.HMAC256(user.password)
        // 附带username信息
        return JWT.create()
            .withAudience(user.uid.toString())
            .withClaim("uid", user.uid)
            .withClaim("username", user.username)
            .withClaim("email", user.email)
            .withClaim("isLocked", user.isLocked)
            .withClaim("role", jacksonObjectMapper().writeValueAsString(user.role))
            .withExpiresAt(date)
            .sign(algorithm)
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param user 用户对象
     * @return 是否正确
     */
    fun verify(token: String, user: User): Boolean {
        val algorithm: Algorithm = Algorithm.HMAC256(user.password)
        val verifier: JWTVerifier = JWT.require(algorithm)
            .withAudience(user.uid.toString())
            .withClaim("uid", user.uid)
            .withClaim("username", user.username)
            .withClaim("email", user.email)
            .withClaim("isLocked", user.isLocked)
            .withClaim("role", jacksonObjectMapper().writeValueAsString(user.role))
            .build()
        verifier.verify(token)
        return true
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    fun getUsername(token: String): String {
        val jwt = JWT.decode(token)
        return jwt.getClaim("username").asString()
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户 email
     */
    fun getEmail(token: String): String {
        val jwt = JWT.decode(token)
        return jwt.getClaim("email").asString()
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户 uid
     */
    fun getUid(token: String): Int {
        val jwt = JWT.decode(token)
        return jwt.audience[0].toInt()
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户 isLocked
     */
    fun getIsLocked(token: String): Boolean {
        val jwt = JWT.decode(token)
        return jwt.getClaim("isLocked").asBoolean()
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户 role
     */
    fun getRole(token: String): CommonRoles {
        val jwt = JWT.decode(token)
        val jsonRole = jwt.getClaim("role").asString()
        return jacksonObjectMapper().readValue(jsonRole, CommonRoles::class.java)
    }
}
