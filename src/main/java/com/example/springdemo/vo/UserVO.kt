package com.example.springdemo.vo

import com.example.springdemo.dao.User
import com.example.springdemo.dao.UserDetail

data class UserVO (
    var user: User,
    val detail: UserDetail
)
