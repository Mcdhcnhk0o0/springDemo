package com.example.springdemo.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.springdemo.dao.User
import java.lang.RuntimeException

class JWTUtil {

    companion object Instance {

        private fun jwtAlgorithm(secret: String): Algorithm {
            return Algorithm.HMAC256(secret)
        }

        fun createToken(userId: String, password: String): String {
            return JWT.create().withAudience(userId).sign(jwtAlgorithm(password))
        }

        fun getUserIdFromToken(token: String): String {
            return JWT.decode(token).audience[0]
        }

        fun verifyUserByToken(user: User, token: String) {
                val jwtVerifier = JWT.require(jwtAlgorithm(user.password!!)).build()
                jwtVerifier.verify(token)
        }

    }

}