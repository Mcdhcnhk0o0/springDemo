package com.example.springdemo.utils

import kotlin.random.Random

object UserIdUtil {

    private const val randomSectionMask = 1000
    private const val randomTotalMask = 1000000

    fun generateUserId(email: String): Long {
        var timeSection = System.currentTimeMillis()
        if (timeSection >= Long.MAX_VALUE / randomTotalMask) {
            timeSection /= randomTotalMask
        }
        val emailSection = email.hashCode() % randomSectionMask
        val randomSection = Random.nextInt(randomSectionMask)
        return timeSection * randomTotalMask + emailSection * randomSectionMask + randomSection
    }


}