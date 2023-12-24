package com.example.springdemo.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.springdemo.bean.dao.User

class JWTUtil {

    companion object Instance {

        private fun jwtAlgorithm(secret: String): Algorithm {
            return Algorithm.HMAC256(secret)
        }

        @JvmStatic
        fun createToken(userId: String, password: String): String {
            return JWT.create().withAudience(userId).sign(jwtAlgorithm(password))
        }

        @JvmStatic
        fun getUserIdStrFromToken(token: String): String {
            return JWT.decode(token).audience[0]
        }

        @JvmStatic
        fun getUserIdFromToken(token: String): Long {
            return JWT.decode(token).audience[0].toLong()
        }

        fun verifyUserByToken(user: User, token: String) {
            val jwtVerifier = JWT.require(jwtAlgorithm(user.password!!)).build()
            jwtVerifier.verify(token)
        }

    }

}