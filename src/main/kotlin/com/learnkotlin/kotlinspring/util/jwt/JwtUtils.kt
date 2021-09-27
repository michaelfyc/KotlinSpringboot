package com.learnkotlin.kotlinspring.util.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.slf4j.LoggerFactory
import java.util.Date

class JwtUtils {
    private val log = LoggerFactory.getLogger(this::class.java)

    // 过期时间5分钟
    private val expireTime = (5 * 60 * 1000).toLong()

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    fun sign(username: String?, secret: String?): String? {
        val date = Date(System.currentTimeMillis() + expireTime)
        val algorithm: Algorithm = Algorithm.HMAC256(secret)
        // 附带username信息
        return JWT.create()
            .withClaim("username", username)
            .withExpiresAt(date)
            .sign(algorithm)
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    fun verify(token: String?, username: String?, secret: String?): Boolean {
        val algorithm: Algorithm = Algorithm.HMAC256(secret)
        val verifier: JWTVerifier = JWT.require(algorithm)
            .withClaim("username", username)
            .build()
        val jwt: DecodedJWT = verifier.verify(token)
        return true
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    fun getUsername(token: String?): String? {
        val jwt = JWT.decode(token)
        return jwt.getClaim("username").asString()
    }
}
