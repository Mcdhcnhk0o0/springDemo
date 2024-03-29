package com.example.springdemo.utils

import com.auth0.jwt.JWT
import kotlin.random.Random

object UserInfoUtil {

    private const val randomSectionMask = 1000
    private const val randomTotalMask = 1000000

    @JvmStatic
    fun generateUserId(email: String): Long {
        var timeSection = System.currentTimeMillis()
        if (timeSection >= Long.MAX_VALUE / randomTotalMask) {
            timeSection /= randomTotalMask
        }
        val emailSection = email.hashCode() % randomSectionMask
        val randomSection = Random.nextInt(randomSectionMask)
        return timeSection * randomTotalMask + emailSection * randomSectionMask + randomSection
    }

    @JvmStatic
    fun parseUserIdFromToken(token: String): Long {
        return JWT.decode(token).audience[0].toLong()
    }


}